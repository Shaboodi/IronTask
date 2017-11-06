import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Created by gal on 26/04/2017<br><br>
 *
 * This class scrapes descriptions for apps in the iTunes App Store,<br>
 * and find the most important words for each description sorted by their TF-IDF score descending.<br>
 * @see <a href="http:////en.wikipedia.org/wiki/Tf–idf">http:////en.wikipedia.org/wiki/Tf–idf</a>
 *
 */
public class ItunesAppsDescriptionWordsRanker {

    private static final String ITUNES_URL = "http://itunes.apple.com/lookup?id=";

    // map between app id and information related to the app description - used for calculating tf
    private Map<String, AppInfo> appsInfoMap = new ConcurrentHashMap<>();

    // map between word and the number of app descriptions it appears - used for calculating idf
    private Map<String, Integer> corpusWordsFrequencies = new HashMap<>();

    private HttpClient client;

    public ItunesAppsDescriptionWordsRanker() {
        this.client = new DefaultHttpClient();
    }

    public void rank(Path path) {
        try (Stream<String> linesStream = Files.lines(path)) {
            linesStream.parallel().forEach(this::extractDescription);
            calculateTfIdfAndPrintResult();
        } catch (IOException e) {
            System.out.println("Exception thrown when trying to read lines from file, " + e.getMessage());
        }
    }

    private void calculateTfIdfAndPrintResult() {
        for (Entry<String, AppInfo> appEntry : appsInfoMap.entrySet()) {
            Map<String, AppInfo.TfIdf> wordsFrequencyAndTFMap = appEntry.getValue().getAppTfIdfMap();

            wordsFrequencyAndTFMap.forEach((key, value) -> {
                Integer wordFrequencyInCorpus = corpusWordsFrequencies.get(key);
                value.setIdf(Math.log(appsInfoMap.entrySet().size() / wordFrequencyInCorpus));
                value.updateTfIdf();
            });

            Stream<Entry<String, AppInfo.TfIdf>> topTen = wordsFrequencyAndTFMap.entrySet().stream()
                                                                            .sorted(Comparator.comparing((Entry<String, AppInfo.TfIdf> entry) -> entry.getValue().getTfIdf()).reversed())
                                                                            .limit(10);
            printApp(appEntry.getKey(), topTen);
        }
    }

    private void printApp(String appId, Stream<Entry<String, AppInfo.TfIdf>> topTen) {
        System.out.print(appId + ": ");
        topTen.parallel().forEach(topTenEntry -> System.out.print(topTenEntry.getKey() + " " + topTenEntry.getValue().getTfIdf() + ", "));
        System.out.println();
    }

    private void extractDescription(String appId) {
        try {
            HttpResponse response = client.execute(new HttpGet(ITUNES_URL + appId));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                    if (response.getStatusLine().getStatusCode() != 200) {
                        System.out.println("Application not found, url: " + ITUNES_URL + appId);
                        return;
                    }
                    StringBuilder responseStr = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        responseStr.append(line);
                    }
                    JSONObject jsonObj = new JSONObject(responseStr.toString());
                    JSONArray array = jsonObj.getJSONArray("results");
                    if (array.length() > 0) {
                        String description = array.getJSONObject(0).getString("description");
                        appsInfoMap.put(appId, new AppInfo(description, corpusWordsFrequencies));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception thrown when trying to fetch app info, AppId: " + appId + ". " + e.getMessage());
        }
    }
}
