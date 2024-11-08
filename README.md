# Hello Minecraft! Launcher 更新分发

[GitHub](https://github.com/HMCL-dev/HMCL-Update) • [Gitee](https://gitee.com/Glavo/HMCL-Update)

本仓库用于维护 HMCL 更新文件下载 CDN，同时提供一个更新源。HMCL 通常会在新版本发布后将它们及时推送给用户，但你也可以手动指定本更新源覆盖默认更新源。


### 稳定版 [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-stable?label=稳定版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-stable/3.5.9/pom)

下载稳定版 v3.5.9:

* `.exe`：[hmcl-stable-3.5.9.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.9/hmcl-stable-3.5.9.exe)
* `.jar`：[hmcl-stable-3.5.9.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.9/hmcl-stable-3.5.9.jar)

你可以在环境变量 `HMCL_JAVA_OPTS` 中添加以下内容使 HMCL 通过本更新源更新：

```
-Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/stable.json
```

### 测试版 [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-dev?label=测试版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-dev/3.5.9.261/pom)

下载测试版 v3.5.9.261:

* `.exe`：[hmcl-dev-3.5.9.261.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.9.261/hmcl-dev-3.5.9.261.exe)
* `.jar`：[hmcl-dev-3.5.9.261.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.9.261/hmcl-dev-3.5.9.261.jar)

你可以在环境变量 `HMCL_JAVA_OPTS` 中添加以下内容使 HMCL 通过本更新源更新：

```
-Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/dev.json
```



[HMCL CI](https://ci.huangyuhui.net/) 提供文件哈希校验码，请自行校验文件完整性。
## 更新文件

以下文件链接用于直接访问 HMCL 更新文件，通常用于 HMCL 自动更新功能，一般用户请忽略。

### 稳定版

稳定版更新文件链接：

* `.jar`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.9/hmcl-stable-3.5.9.jar
* `.json`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.9/hmcl-stable-3.5.9.json

### 测试版

测试版更新文件链接：

* `.jar`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.9.261/hmcl-dev-3.5.9.261.jar
* `.json`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.9.261/hmcl-dev-3.5.9.261.json

