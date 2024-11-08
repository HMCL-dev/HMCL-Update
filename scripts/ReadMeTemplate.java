/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

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
                            
                            * `.exe`：[%1$s-%2$s.exe](https://mirrors.cloud.tencent.com/nexus/repository/maven-public/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.exe)
                            * `.jar`：[%1$s-%2$s.jar](https://mirrors.cloud.tencent.com/nexus/repository/maven-public/org/glavo/hmcl/%1$s/%2$s/%1$s-%2$s.jar)
                            
                            你可以在环境变量 `HMCL_JAVA_OPTS` 中添加以下内容使 HMCL 通过本更新源更新：
                            
                            ```
                            -Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/%3$s.json
                            ```
                            
                            """.formatted(channel.artifactId(), version, channel.name(), channel.chineseName()));
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
    }
}
