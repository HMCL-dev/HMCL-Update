import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateJson {
    public static void main(String[] args) throws Exception {
        Path jsonDir = Paths.get("update");
        if (Files.notExists(jsonDir)) {
            Files.createDirectory(jsonDir);
        }

        Map<Channel, String> versions = Channel.fetchVersions(false);

        AtomicBoolean commit = new AtomicBoolean(false);

        versions.forEach((channel, version) -> {
            try {
                URL remoteJson = new URL("https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.json".formatted(channel.artifactId(), version));
                System.out.println("Fetch update json from " + remoteJson);

                HttpURLConnection connection = (HttpURLConnection) remoteJson.openConnection();
                int responseCode = connection.getResponseCode();

                if (responseCode == 404) {
                    System.err.println("更新 JSON 不存在");
                    return;
                }

                if (responseCode != 200) {
                    System.err.println("获取更新 JSON 失败，错误码：" + responseCode);
                    try (InputStream errorStream = connection.getErrorStream()) {
                        if (errorStream != null) {
                            errorStream.transferTo(System.err);
                        }
                    }
                    return;
                }

                String jsonInfo;
                try (InputStream input = connection.getInputStream()) {
                    jsonInfo = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                }

                Path jsonPath = jsonDir.resolve(channel.name() + ".json");
                if (Files.exists(jsonPath)) {
                    String oldValue = Files.readString(jsonPath);
                    if (jsonInfo.equals(oldValue)) {
                        System.out.println(channel.name() + " 未更新");
                        return;
                    }
                }

                Files.writeString(jsonPath, jsonInfo);
                commit.set(true);

            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        });

        if (commit.get()) {
            GitHubUtils.addEnv("COMMIT_CHANGE", "true");
        }
    }
}
