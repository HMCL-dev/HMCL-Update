# Hello Minecraft! Launcher 更新分发

本仓库用于将 HMCL 推送至 Maven Central 上，并获取阿里云镜像链接。

[![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-stable?label=稳定版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-stable/3.5.2/pom)
[![](https://img.shields.io/maven-central/v/org.glavo.hmcl/hmcl-dev?label=测试版)](https://search.maven.org/artifact/org.glavo.hmcl/hmcl-dev/3.5.0.214/pom)

下载稳定版 v3.5.2:

* `.exe`：[hmcl-stable-3.5.2.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.exe)
* `.jar`：[hmcl-stable-3.5.2.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.jar)

下载测试版 v3.5.0.214:

* `.exe`：[hmcl-dev-3.5.0.214.exe](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.0.214/hmcl-dev-3.5.0.214.exe)
* `.jar`：[hmcl-dev-3.5.0.214.jar](https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.0.214/hmcl-dev-3.5.0.214.jar)



[HMCL CI](https://ci.huangyuhui.net/) 提供文件 SHA-1 校验码，请自行校验文件完整性。
## 更新文件

下面的链接用于 HMCL 自动更新功能。

稳定版更新文件链接：

* `.jar`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.jar
* `.pack`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.pack
* `.pack.gz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.pack.gz
* `.pack.xz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.pack.xz

您可以通过在启动 HMCL 时添加以下 JVM 参数覆盖默认更新源，强制通过上方的 CDN 链接更新至 HMCL 3.5.2：

```
-Dhmcl.update_source.override=https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-stable/3.5.2/hmcl-stable-3.5.2.json
```

测试版更新文件链接：

* `.jar`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.0.214/hmcl-dev-3.5.0.214.jar
* `.pack`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.0.214/hmcl-dev-3.5.0.214.pack
* `.pack.gz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.0.214/hmcl-dev-3.5.0.214.pack.gz
* `.pack.xz`：https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.0.214/hmcl-dev-3.5.0.214.pack.xz

您可以通过在启动 HMCL 时添加以下 JVM 参数覆盖默认更新源，强制通过上方的 CDN 链接更新至 HMCL 3.5.0.214：

```
-Dhmcl.update_source.override=https://maven.aliyun.com/repository/central/org/glavo/hmcl/hmcl-dev/3.5.0.214/hmcl-dev-3.5.0.214.json
```

