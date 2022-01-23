import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("CodeBlock2Expr")
public class ReadMeTemplate {
    public static void main(String[] args) throws Exception {

        Map<Channel, String> versions = Channel.fetchVersions(true);
        StringBuilder builder = new StringBuilder();

        builder.append("# Hello Minecraft! Launcher 更新分发\n\n")
                .append("本仓库用于将 HMCL 推送至 Maven Central 上，并获取阿里云镜像链接。\n\n");

        versions.forEach((channel, version) -> {
            builder.append(
                    "[![](https://img.shields.io/maven-central/v/org.glavo.hmcl/%1$s?label=%3$s)](https://search.maven.org/artifact/org.glavo.hmcl/%1$s/%2$s/pom)\n".formatted(
                            channel.artifactId(), version, channel.chineseName()
                    ));
        });

        builder.append('\n');

        versions.forEach((channel, version) -> {
            builder.append("下载%s v%s:\n\n".formatted(channel.chineseName(), version))
                    .append("""
                            * `.exe`：[%1$s-%2$s.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.exe)
                            * `.jar`：[%1$s-%2$s.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.jar)
                                                        
                            """.formatted(channel.artifactId(), version));
        });

        builder.append("\n\n");

        builder.append("[HMCL CI](https://ci.huangyuhui.net/) 提供文件 SHA-1 校验码，请自行校验文件完整性。\n");

        builder.append("## 更新文件\n\n")
                .append("下面的链接用于 HMCL 自动更新功能。我们通常会在发布后将它们及时推送给用户，但您也可以手动指定它们作为更新源强制更新。\n\n");

        versions.forEach((channel, version) -> {
            builder.append("""
                    ### %3$s
                                        
                    %3$s更新文件链接：
                                        
                    * `.jar`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.jar
                    * `.pack`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.pack
                    * `.pack.gz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.pack.gz
                    * `.pack.xz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.pack.xz
                                        
                    您可以通过在启动 HMCL 时添加以下 JVM 参数覆盖默认更新源，强制通过上方的 CDN 链接更新至 HMCL %2$s：
                                        
                    ```
                    -Dhmcl.update_source.override=https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.json
                    ```
                                        
                    """.formatted(channel.artifactId(), version, channel.chineseName()));
        });

        String res = builder.toString();

        var file = Paths.get("README.md");

        if (Files.exists(file) && Files.readString(file).equals(res)) {
            System.out.println("README has not changed");
            return;
        }

        Files.writeString(file, res, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        GitHubUtils.addEnv("COMMIT_CHANGE", "true");

        Stream.of("exe", "jar", "pack", "pack.xz", "pack.gz", "json")
                .map(ext -> "https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s." + ext)
                .flatMap(template -> versions.entrySet().stream().map(entry -> template.formatted(entry.getKey().artifactId(), entry.getValue())))
                .forEach(urlStr -> {
                    try {
                        URL url = new URL(urlStr);
                        System.out.println("开始预热 " + url);
                        try (var input = url.openStream()) {
                            input.readNBytes(10);
                        }
                    } catch (Throwable ex) {
                        System.out.printf("预热 %s 时发生错误%n", urlStr);
                        ex.printStackTrace();
                    }
                });
    }



    private static String last(String[] args) {
        return args[args.length - 1];
    }
}
