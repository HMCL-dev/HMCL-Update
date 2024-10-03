# Hello Minecraft! Launcher 更新分发

[GitHub](https://github.com/HMCL-dev/HMCL-Update) • [Gitee](https://gitee.com/Glavo/HMCL-Update)

本仓库用于维护 HMCL 更新文件下载 CDN，同时提供一个非官方更新源。HMCL 官方通常会在新版本发布后将它们及时推送给用户，但您也可以手动指定本更新源覆盖官方更新源。


### 稳定版 [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-stable?label=稳定版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-stable/3.5.9/pom)

下载稳定版 v3.5.9:

* `.exe`：[hmcl-stable-3.5.9.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.9/hmcl-stable-3.5.9.exe)
* `.jar`：[hmcl-stable-3.5.9.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.9/hmcl-stable-3.5.9.jar)

您可以下载脚本 [stable.bat](https://gitee.com/Glavo/HMCL-Update/attach_files/957979/download/stable.bat)，通过运行它自动指定 HMCL 使用本更新源更新至最新稳定版。

除此之外，您也可以手动在环境变量 `JAVA_TOOL_OPTIONS` 中添加以下内容实现与脚本相同的功能：

```
-Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/stable.json
```

此脚本非 HMCL 官方提供，您可以在 [update/stable.bat](update/stable.bat) 中查看其源码，请自行校验完整性与安全性。

### 测试版 [![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-dev?label=测试版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-dev/3.5.9.255/pom)

下载测试版 v3.5.9.255:

* `.exe`：[hmcl-dev-3.5.9.255.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.9.255/hmcl-dev-3.5.9.255.exe)
* `.jar`：[hmcl-dev-3.5.9.255.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.9.255/hmcl-dev-3.5.9.255.jar)

您可以下载脚本 [dev.bat](https://gitee.com/Glavo/HMCL-Update/attach_files/957978/download/dev.bat)，通过运行它自动指定 HMCL 使用本更新源更新至最新测试版。

除此之外，您也可以手动在环境变量 `JAVA_TOOL_OPTIONS` 中添加以下内容实现与脚本相同的功能：

```
-Dhmcl.update_source.override=https://gitee.com/Glavo/HMCL-Update/raw/main/update/dev.json
```

此脚本非 HMCL 官方提供，您可以在 [update/dev.bat](update/dev.bat) 中查看其源码，请自行校验完整性与安全性。

**设置下载源后，HMCL 会忽略官方下载源。删除环境变量 'JAVA_TOOL_OPTIONS' 即可以恢复使用官方源。（如果您自定义了 `JAVA_TOOL_OPTIONS` 环境变量，从其中删除 `-Dhmcl.update_source.override=...` 一项即可。）**

[HMCL CI](https://ci.huangyuhui.net/) 提供文件哈希校验码，请自行校验文件完整性。
## 更新文件

以下文件链接用于直接访问 HMCL 更新文件，通常用于 HMCL 自动更新功能，一般用户请忽略。

### 稳定版

稳定版更新文件链接：

* `.jar`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.9/hmcl-stable-3.5.9.jar
* `.json`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.9/hmcl-stable-3.5.9.json

### 测试版

测试版更新文件链接：

* `.jar`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.9.255/hmcl-dev-3.5.9.255.jar
* `.json`: https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.9.255/hmcl-dev-3.5.9.255.json

