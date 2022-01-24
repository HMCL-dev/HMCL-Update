# Hello Minecraft! Launcher 更新分发

[GitHub](https://github.com/Glavo/HMCL-Update) · [Gitee](https://gitee.com/Glavo/HMCL-Update)

本仓库用于维护 HMCL 更新源 CDN。我们通常会在新版本发布后将它们及时推送给用户，但您也可以手动指定本更新源覆盖默认更新源。


### 稳定版 [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-stable?label=稳定版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-stable/3.5.3/pom)

下载稳定版 v3.5.3:

* `.exe`：[hmcl-stable-3.5.3.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.3/hmcl-stable-3.5.3.exe)
* `.jar`：[hmcl-stable-3.5.3.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.3/hmcl-stable-3.5.3.jar)

您可以在环境变量 `JAVA_TOOL_OPTIONS` 中添加以下内容，指定 HMCL 使用本更新源更新至最新稳定版：

```
-Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/stable.json
```

### 测试版 [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-dev?label=测试版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-dev/3.5.2.218/pom)

下载测试版 v3.5.2.218:

* `.exe`：[hmcl-dev-3.5.2.218.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.218/hmcl-dev-3.5.2.218.exe)
* `.jar`：[hmcl-dev-3.5.2.218.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.218/hmcl-dev-3.5.2.218.jar)

您可以在环境变量 `JAVA_TOOL_OPTIONS` 中添加以下内容，指定 HMCL 使用本更新源更新至最新测试版：

```
-Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/dev.json
```



[HMCL CI](https://ci.huangyuhui.net/) 提供文件哈希校验码，请自行校验文件完整性。
## 更新文件

以下文件链接用于直接访问 HMCL 更新文件，通常用于 HMCL 自动更新功能，一般用户请忽略。

### 稳定版

稳定版更新文件链接：

* `.jar`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.3/hmcl-stable-3.5.3.jar
* `.pack`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.3/hmcl-stable-3.5.3.pack
* `.pack.gz`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.3/hmcl-stable-3.5.3.pack.gz
* `.pack.xz`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.3/hmcl-stable-3.5.3.pack.xz
* `.json`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.3/hmcl-stable-3.5.3.json

### 测试版

测试版更新文件链接：

* `.jar`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.218/hmcl-dev-3.5.2.218.jar
* `.pack`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.218/hmcl-dev-3.5.2.218.pack
* `.pack.gz`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.218/hmcl-dev-3.5.2.218.pack.gz
* `.pack.xz`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.218/hmcl-dev-3.5.2.218.pack.xz
* `.json`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.218/hmcl-dev-3.5.2.218.json

