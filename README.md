# Hello Minecraft! Launcher 更新分发

[GitHub](https://github.com/HMCL-dev/HMCL-Update) • [Gitee](https://gitee.com/Glavo/HMCL-Update)

本仓库用于维护 HMCL 更新文件下载 CDN，同时提供一个更新源。HMCL 通常会在新版本发布后将它们及时推送给用户，但你也可以手动指定本更新源覆盖默认更新源。


## 稳定版 [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-stable?label=稳定版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-stable/3.6.12/pom)

下载稳定版 v3.6.12:

* `.exe`：[hmcl-stable-3.6.12.exe](https://mirrors.cloud.tencent.com/nexus/repository/maven-public/org/glavo/hmcl/hmcl-stable/3.6.12/hmcl-stable-3.6.12.exe)
* `.jar`：[hmcl-stable-3.6.12.jar](https://mirrors.cloud.tencent.com/nexus/repository/maven-public/org/glavo/hmcl/hmcl-stable/3.6.12/hmcl-stable-3.6.12.jar)

你可以在环境变量 `HMCL_JAVA_OPTS` 中添加以下内容使 HMCL 通过本更新源更新：

```
-Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/stable.json
```

## 开发版 [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-dev?label=开发版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-dev/3.6.12.278/pom)

下载开发版 v3.6.12.278:

* `.exe`：[hmcl-dev-3.6.12.278.exe](https://mirrors.cloud.tencent.com/nexus/repository/maven-public/org/glavo/hmcl/hmcl-dev/3.6.12.278/hmcl-dev-3.6.12.278.exe)
* `.jar`：[hmcl-dev-3.6.12.278.jar](https://mirrors.cloud.tencent.com/nexus/repository/maven-public/org/glavo/hmcl/hmcl-dev/3.6.12.278/hmcl-dev-3.6.12.278.jar)

你可以在环境变量 `HMCL_JAVA_OPTS` 中添加以下内容使 HMCL 通过本更新源更新：

```
-Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/dev.json
```

