import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("CodeBlock2Expr")
public class ReadMeTemplate {
    private static final Pattern VERSION_PATTERN = Pattern.compile("^[0-9]+\\.[0-9]+\\.[0-9]+(-[0-9]+)?$");

    record Channel(String name, String chineseName, String ciUrlBase) {
        static final List<Channel> channels;

        static {
            try {
                Path path = Paths.get("channels.properties");
                if (Files.notExists(path)) {
                    throw new IllegalStateException(path + " does not exists");
                }

                Properties properties = new Properties();
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    properties.load(reader);
                }

                String[] names = properties.getProperty("names").split(",");

                ArrayList<Channel> cs = new ArrayList<>();
                for (String name : names) {
                    String urlBase = properties.getProperty(name + ".ci.url");
                    if (urlBase == null) {
                        urlBase = "https://ci.huangyuhui.net/job/HMCL-" + name;
                    }

                    String chineseName = properties.getProperty(name + ".name.chinese");

                    cs.add(new Channel(name, chineseName, urlBase));
                }
                channels = cs;
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        String artifactId() {
            return "hmcl-" + name;
        }
    }

    public static void main(String[] args) throws Exception {

        LinkedHashMap<Channel, String> versions = new LinkedHashMap<>();

        for (Channel channel : Channel.channels) {
            URL metadata = new URL("https://repo1.maven.org/maven2/org/glavo/hmcl/%s/maven-metadata.xml".formatted(channel.artifactId()));
            System.out.println("Fetch maven metadata from " + metadata);

            HttpURLConnection connection = (HttpURLConnection) metadata.openConnection();
            int responseCode = connection.getResponseCode();

            if (responseCode == 404) {
                System.out.println("Maven repo does not exist temporarily, ignored");
                continue;
            }

            if (responseCode != 200) {
                System.err.println("Failed to get metadata, response code: " + responseCode);
                try (InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        errorStream.transferTo(System.err);
                    }
                }
                System.exit(1);
                return;
            }

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document;
            try (InputStream input = connection.getInputStream()) {
                document = builder.parse(input);
            }

            String latestVersion = document.getElementsByTagName("latest").item(0).getFirstChild().getNodeValue();

            versions.put(channel, latestVersion);
        }

        StringBuilder builder = new StringBuilder();

        builder.append("# Hello Minecraft! Launcher 更新分发\n\n")
                .append("本仓库用于将 HMCL 推送至 Maven Central 上，并获取阿里云镜像链接。\n\n");

        versions.forEach((channel, version) -> {
            builder.append(
                    "[![](https://img.shields.io/maven-central/v/org.glavo.hmcl/%1$s?label=%3$s)](https://search.maven.org/artifact/org.glavo.hmcl/%1$s/%2$s/pom)\n".formatted(
                            channel.artifactId(), version, channel.chineseName
                    ));
        });

        builder.append('\n');

        versions.forEach((channel, version) -> {
            builder.append("下载%s v%s:\n\n".formatted(channel.chineseName, version))
                    .append("""
                            * `.exe`：[%1$s-%2$s.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.exe)
                            * `.jar`：[%1$s-%2$s.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.jar)
                            
                            """.formatted(channel.artifactId(), version));
        });

        builder.append("\n\n");

        builder.append("[HMCL CI](https://ci.huangyuhui.net/) 提供文件 SHA-1 校验码，请自行校验文件完整性。\n");

        builder.append("## 更新文件\n\n")
                .append("下面的链接用于 HMCL 自动更新功能。\n\n");

        versions.forEach((channel, version) -> {
            builder.append("""
                    %3$s更新文件链接：
                                        
                    * `.jar`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.jar
                    * `.pack`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.pack
                    * `.pack.gz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.pack.gz
                    * `.pack.xz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.pack.xz
                                        
                    您可以通过在启动 HMCL 时添加以下 JVM 参数覆盖默认更新源，强制通过上方的 CDN 链接更新至 HMCL %2$s：
                                        
                    ```
                    -Dhmcl.update_source.override=https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.json
                    ```
                                        
                    """.formatted(channel.artifactId(), version, channel.chineseName));
        });

        String res = builder.toString();

        var file = Paths.get("README.md");

        if (Files.exists(file) && Files.readString(file).equals(res)) {
            System.out.println("README has not changed");
            return;
        }

        Files.writeString(file, res, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        addEnv("COMMIT_CHANGE", "true");
    }

    private static void addEnv(String name, String value) throws Exception {
        String envFile = System.getenv("GITHUB_ENV");
        if (envFile == null) {
            System.out.println("Not a GitHub Action environment, skip exporting environment variables");
            return;
        }

        Files.writeString(Paths.get(envFile), "%s=%s\n".formatted(name, value), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }

    private static String last(String[] args) {
        return args[args.length - 1];
    }
}
