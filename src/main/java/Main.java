import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by gal on 26/04/2017
 */
public class Main {
    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            System.out.println("Usage: Main <filepath>");
            return;
        }
        Path filePath = Paths.get(args[0]);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            System.out.println("File path doesn't exist or file is not readable, path: " + filePath);
            return;
        }
        new ItunesAppsDescriptionWordsRanker().rank(filePath);
    }
}
