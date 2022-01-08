# Hello Minecraft! Launcher 更新分发

本仓库用于将 HMCL 推送至 Maven Central 上，并获取阿里云镜像链接。

[![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-stable?label=稳定版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-stable/3.5.2/pom)
[![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-dev?label=测试版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-dev/3.5.2.216/pom)

下载稳定版 v3.5.2:

* `.exe`：<a href="https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.exe" download="HMCL-3.5.2.exe" rel="nofollow">HMCL-3.5.2.exe</a>
* `.jar`：<a href="https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.jar" download="HMCL-3.5.2.jar" rel="nofollow">HMCL-3.5.2.jar</a>

下载测试版 v3.5.2.216:

* `.exe`：<a href="https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.216/hmcl-dev-3.5.2.216.exe" download="HMCL-3.5.2.216.exe" rel="nofollow">HMCL-3.5.2.216.exe</a>
* `.jar`：<a href="https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.216/hmcl-dev-3.5.2.216.jar" download="HMCL-3.5.2.216.jar" rel="nofollow">HMCL-3.5.2.216.jar</a>



[HMCL CI](https://ci.huangyuhui.net/) 提供文件 SHA-1 校验码，请自行校验文件完整性。
## 更新文件

下面的链接用于 HMCL 自动更新功能。我们通常会在发布后将它们及时推送给用户，但您也可以手动指定它们作为更新源强制更新。

### 稳定版

稳定版更新文件链接：

* `.jar`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.jar
* `.pack`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.pack
* `.pack.gz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.pack.gz
* `.pack.xz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.pack.xz

您可以通过在启动 HMCL 时添加以下 JVM 参数覆盖默认更新源，强制通过上方的 CDN 链接更新至 HMCL 3.5.2：

```
-Dhmcl.update_source.override=https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.json
```

### 测试版

测试版更新文件链接：

* `.jar`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.216/hmcl-dev-3.5.2.216.jar
* `.pack`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.216/hmcl-dev-3.5.2.216.pack
* `.pack.gz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.216/hmcl-dev-3.5.2.216.pack.gz
* `.pack.xz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.216/hmcl-dev-3.5.2.216.pack.xz

您可以通过在启动 HMCL 时添加以下 JVM 参数覆盖默认更新源，强制通过上方的 CDN 链接更新至 HMCL 3.5.2.216：

```
-Dhmcl.update_source.override=https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.2.216/hmcl-dev-3.5.2.216.json
```

