import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtils {
    public static boolean writeIfChanged(Path path, String value) throws IOException {
        if (Files.notExists(path) || !Files.readString(path).equals(value)) {
            Files.writeString(path, value);
            return true;
        } else {
            return false;
        }
    }
}
