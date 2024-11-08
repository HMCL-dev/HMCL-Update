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

        builder.append("""
                # Hello Minecraft! Launcher 更新分发

                [GitHub](https://github.com/HMCL-dev/HMCL-Update) • [Gitee](https://gitee.com/Glavo/HMCL-Update)

                本仓库用于维护 HMCL 更新文件下载 CDN，同时提供一个更新源。HMCL 通常会在新版本发布后将它们及时推送给用户，但你也可以手动指定本更新源覆盖默认更新源。


                """);

        versions.forEach((channel, version) -> {
            builder.append("""
                            ### %4$s [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/%1$s?label=%4$s)](https://search.maven.org/artifact/org.glavo.hmcl/%1$s/%2$s/pom)
                            
                            下载%4$s v%2$s:
                            
                            * `.exe`：[%1$s-%2$s.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.exe)
                            * `.jar`：[%1$s-%2$s.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.jar)
                            
                            你可以在环境变量 `HMCL_JAVA_OPTS` 中添加以下内容使 HMCL 通过本更新源更新：
                            
                            ```
                            -Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/%3$s.json
                            ```
                            
                            """.formatted(channel.artifactId(), version, channel.name(), channel.chineseName()));
        });

        builder.append("\n\n[HMCL CI](https://ci.huangyuhui.net/) 提供文件哈希校验码，请自行校验文件完整性。\n");

        builder.append("## 更新文件\n\n")
                .append("以下文件链接用于直接访问 HMCL 更新文件，通常用于 HMCL 自动更新功能，一般用户请忽略。\n\n");

        versions.forEach((channel, version) -> {
            builder.append("""
                    ### %3$s
                    
                    %3$s更新文件链接：
                    
                    * `.jar`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.jar
                    * `.json`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.json

                    """.formatted(channel.artifactId(), version, channel.chineseName()));
        });

        String res = builder.toString();

        var file = Paths.get("README.md");

        if (!FileUtils.writeIfChanged(file, res)) {
            System.out.println("README has not changed");
            return;
        }

        UpdateJson.main(new String[]{});

        Files.writeString(file, res, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        GitHubUtils.addEnv("COMMIT_CHANGE", "true");

        Stream.of("exe", "jar", "json")
                .parallel()
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
                        synchronized (System.err) {
                            System.out.printf("预热 %s 时发生错误%n", urlStr);
                            ex.printStackTrace();
                        }
                    }
                });
    }



    private static String last(String[] args) {
        return args[args.length - 1];
    }
}
