import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class GitHubUtils {
    public static void addEnv(String name, String value) throws Exception {
        String envFile = System.getenv("GITHUB_ENV");
        if (envFile == null) {
            System.out.println("Not a GitHub Action environment, skip exporting environment variables");
            return;
        }

        Files.writeString(Paths.get(envFile), "%s=%s\n".formatted(name, value), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }
}
