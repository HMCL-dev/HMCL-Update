import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.regex.Pattern;

public class CheckExistingVersion {
    private static final Pattern PATTERN = Pattern.compile("^[0-9]+\\.[0-9]+\\.[0-9]+(\\.[0-9]+)?$");


    public static void main(String[] args) throws Exception {
        String version = System.getenv("HMCL_VERSION");
        if (!PATTERN.matcher(version).matches()) {
            throw new IllegalArgumentException("Bag HMCL version: " + version);
        }

        String channel = Objects.requireNonNull(System.getenv("HMCL_UPDATE_CHANNEL"), "channel");

        URL metadata = new URL("https://repo1.maven.org/maven2/org/glavo/hmcl/hmcl-%s/maven-metadata.xml".formatted(channel));
        System.out.println("Fetch maven metadata from " + metadata);

        HttpURLConnection connection = (HttpURLConnection) metadata.openConnection();
        int responseCode = connection.getResponseCode();

        if (responseCode == 404) {
            System.out.println("Maven repo does not exist temporarily, should be updated");
            addEnv("need_update", "true");
            return;
        }

        if (responseCode != 200) {
            System.err.println("Failed to get metadata, response code: " + responseCode);
            try (InputStream errorStream = connection.getErrorStream()) {
                if (errorStream != null) {
                    errorStream.transferTo(System.err);
                }
            }
            System.exit(1);
            return;
        }

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document;
        try (InputStream input = connection.getInputStream()) {
            document = builder.parse(input);
        }

        NodeList versionList = document.getElementsByTagName("version");
        int length = versionList.getLength();

        for (int i = 0; i < length; i++) {
            Node item = versionList.item(i);
            if (version.equals(item.getFirstChild().getNodeValue())) {
                System.out.printf("%s already exists, no update required%n", version);
                return;
            }
        }

        addEnv("need_update", "true");
    }


    private static final Path GITHUB_ENV_FILE = Paths.get(System.getenv("GITHUB_ENV"));

    private static void addEnv(String name, String value) throws Exception {
        Files.writeString(GITHUB_ENV_FILE, "%s=%s\n".formatted(name, value), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }
}
