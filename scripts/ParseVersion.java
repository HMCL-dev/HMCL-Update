import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ParseVersion {
    private static final Pattern PATTERN = Pattern.compile("^HMCL-(?<version>(?<major>[0-9]+)\\.(?<minor>[0-9]+)\\.(?<patch>[0-9]+)(\\.(?<build>[0-9]+))?)\\.(exe|jar)$");

    public static void main(String[] args) throws Exception {
        boolean isDev;
        if ("dev".equals(args[0])) {
            isDev = true;
        } else if ("stable".equals(args[0])) {
            isDev = false;
        } else {
            System.err.println("Unknown channel: " + args[0]);
            System.exit(-1);
            return;
        }

        String fileName = args[1];

        Matcher matcher = PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            System.err.printf("Bad file name: '%s'%n", fileName);
            System.exit(-1);
        }

        String version = matcher.group("version");

        String majorVersion = matcher.group("major");
        String minorVersion = matcher.group("minor");
        String patchVersion = matcher.group("patch");
        String buildNumber = matcher.group("build");

        addEnv("HMCL_VERSION", version);
        addEnv("HMCL_MAJOR_VERSION", majorVersion);
        addEnv("HMCL_MINOR_VERSION", minorVersion);
        addEnv("HMCL_PATCH_VERSION", patchVersion);

        if (buildNumber != null) {
            addEnv("HMCL_BUILD_NUMBER", buildNumber);
        }

        if (isDev) {
            addEnv("HMCL_DOWNLOAD_BASE", "https://ci.huangyuhui.net/job/HMCL/lastSuccessfulBuild/artifact/HMCL/build/libs");
        } else {
            addEnv("HMCL_DOWNLOAD_BASE", "https://ci.huangyuhui.net/job/HMCL-stable/lastSuccessfulBuild/artifact/HMCL/build/libs");
        }
    }

    private static final Path GITHUB_ENV_FILE = Paths.get(System.getenv("GITHUB_ENV"));

    private static void addEnv(String name, String value) throws Exception {
        Files.writeString(GITHUB_ENV_FILE, "%s=%s\n".formatted(name, value), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }
}