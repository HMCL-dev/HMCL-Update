import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class ReadMeTemplate {
    private static final Pattern VERSION_PATTERN = Pattern.compile("^[0-9]+\\.[0-9]+\\.[0-9]+(-[0-9]+)?$");

    public static void main(String[] args) throws IOException {
        String devVersion = Objects.requireNonNull(System.getenv("HMCL_DEV_VERSION"), "devVersion");
        String stableVersion = Objects.requireNonNull(System.getenv("HMCL_STABLE_VERSION"), "stableVersion");

        if (!devVersion.isEmpty() && !VERSION_PATTERN.matcher(devVersion).matches()) {
            System.err.println("Bad devVersion " + devVersion);
        }
        if (!stableVersion.isEmpty() && !VERSION_PATTERN.matcher(stableVersion).matches()) {
            System.err.println("Bad stableVersion " + stableVersion);
        }

        Map<String, String> vars = Map.of(
                "HMCL_DEV_NPM_VERSION", devVersion,
                "HMCL_DEV_VERSION", devVersion.replace('-', '.'),
                "HMCL_STABLE_NPM_VERSION", stableVersion,
                "HMCL_STABLE_VERSION", stableVersion.replace('-', '.')

        );

        String readme = """
                # Hello Minecraft! Launcher 更新分发
                
                [![](https://data.jsdelivr.com/v1/package/npm/hmcl-update-stable/badge)](https://www.jsdelivr.com/package/npm/hmcl-update-stable)
                [![](https://data.jsdelivr.com/v1/package/npm/hmcl-update/badge)](https://www.jsdelivr.com/package/npm/hmcl-update)
                
                最新稳定版：[v@@HMCL_STABLE_VERSION@@](https://www.npmjs.com/package/hmcl-update-stable/v/@@HMCL_STABLE_NPM_VERSION@@)
                最新测试版：[v@@HMCL_DEV_VERSION@@](https://www.npmjs.com/package/hmcl-update/v/@@HMCL_DEV_NPM_VERSION@@)
                
                用于将 [Hello Minecraft! Launcher](https://github.com/huanghongxun/HMCL) 推送至 NPM 仓库中，并获取 CDN 下载链接。
                                
                下载 HMCL 最新稳定版（tgz 格式，包括 exe 和 jar）：
                
                * [NPM 官方源](https://www.npmjs.com/)：[hmcl-update-stable-@@HMCL_STABLE_NPM_VERSION@@.tgz](https://registry.npmjs.org/hmcl-update-stable/-/hmcl-update-@@HMCL_STABLE_NPM_VERSION@@.tgz)
                * [淘宝 NPM 镜像](https://npmmirror.com/)：[hmcl-update-stable-@@HMCL_STABLE_NPM_VERSION@@.tgz](https://registry.npmjs.org/hmcl-update-stable/-/hmcl-update-@@HMCL_STABLE_NPM_VERSION@@.tgz)
                
                下载 HMCL 最新测试版（tgz 格式，包括 exe 和 jar）：
                
                * [NPM 官方源](https://www.npmjs.com/)：[hmcl-update-@@HMCL_DEV_NPM_VERSION@@.tgz](https://registry.npmjs.org/hmcl-update/-/hmcl-update-@@HMCL_DEV_NPM_VERSION@@.tgz)
                * [淘宝 NPM 镜像](https://npmmirror.com/)：[hmcl-update-@@HMCL_DEV_NPM_VERSION@@.tgz](https://registry.npmjs.org/hmcl-update/-/hmcl-update-@@HMCL_DEV_NPM_VERSION@@.tgz)
                
                **注意，淘宝镜像在国内速度更快，但更新比官方慢，如果无法下载请稍等，镜像源需要一段时间来同步。**
                
                稳定版更新文件链接（因为 jsDelivr 禁止下载扩展名为 jar 的文件，这里使用 zip 扩展名作为替代）：
                
                * `.jar`: https://cdn.jsdelivr.net/npm/hmcl-update-stable@@@HMCL_STABLE_NPM_VERSION@@/HMCL-@@HMCL_STABLE_VERSION@@.zip
                * `.pack`: https://cdn.jsdelivr.net/npm/hmcl-update-stable@@@HMCL_STABLE_NPM_VERSION@@/HMCL-@@HMCL_STABLE_VERSION@@.pack
                * `.pack.gz`: https://cdn.jsdelivr.net/npm/hmcl-update-stable@@@HMCL_STABLE_NPM_VERSION@@/HMCL-@@HMCL_STABLE_VERSION@@.pack.gz
                * `.pack.xz`: https://cdn.jsdelivr.net/npm/hmcl-update-stable@@@HMCL_STABLE_NPM_VERSION@@/HMCL-@@HMCL_STABLE_VERSION@@.pack.xz
                
                测试版更新文件链接（因为 jsDelivr 禁止下载扩展名为 jar 的文件，这里使用 zip 扩展名作为替代）：
                
                * `.jar`: https://cdn.jsdelivr.net/npm/hmcl-update@@@HMCL_DEV_NPM_VERSION@@/HMCL-@@HMCL_DEV_VERSION@@.zip
                * `.pack`: https://cdn.jsdelivr.net/npm/hmcl-update@@@HMCL_DEV_NPM_VERSION@@/HMCL-@@HMCL_DEV_VERSION@@.pack
                * `.pack.gz`: https://cdn.jsdelivr.net/npm/hmcl-update@@@HMCL_DEV_NPM_VERSION@@/HMCL-@@HMCL_DEV_VERSION@@.pack.gz
                * `.pack.xz`: https://cdn.jsdelivr.net/npm/hmcl-update@@@HMCL_DEV_NPM_VERSION@@/HMCL-@@HMCL_DEV_VERSION@@.pack.xz
                
                文件 SHA1 校验码可前往 [HMCL CI](https://ci.huangyuhui.net/) 自行查询校验。
                """;

        for (Map.Entry<String, String> entry : vars.entrySet()) {
            readme = readme.replace("@@%s@@".formatted(entry.getKey()), entry.getValue());
        }

        Files.writeString(Path.of("README.md"), readme);
    }
}
