import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

/**
 * Created by gal on 26/04/2017
 */
public class AppInfo {

    private Map<String, TfIdf> appTfIdfMap = new HashMap<>();

    public AppInfo(String description, Map<String, Integer> corpusWordsFrequencies) {
        String[] wordsArray = description.split("\\W+");
        fillMaps(corpusWordsFrequencies, wordsArray);
    }

    private void fillMaps(Map<String, Integer> corpusWordsFrequencies, String[] wordsArray) {
        Arrays.stream(wordsArray)
              .map(String::toLowerCase)
              .filter(word -> word.length() > 1)
              .collect(groupingBy(Function.identity(), summingInt(e -> 1)))
              .forEach((word, frequency) -> {
                  appTfIdfMap.put(word, new TfIdf(frequency.doubleValue() / wordsArray.length));
                  corpusWordsFrequencies.merge(word, 1, (a, b) -> a + b);
              });
    }

    public Map<String, TfIdf> getAppTfIdfMap() {
        return appTfIdfMap;
    }

    public static class TfIdf {
        private double tf;
        private double idf;
        private double tfIdf;

        public TfIdf(double tf) {
            this.tf = tf;
        }

        public void setIdf(double idf) {
            this.idf = idf;
        }

        public double getIdf() {
            return idf;
        }

        public double getTf() {
            return tf;
        }

        public void updateTfIdf() {
            tfIdf = tf * idf;
        }

        public double getTfIdf() {
            return tfIdf;
        }
    }
}
