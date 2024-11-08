import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public record Channel(String name, String chineseName, String ciUrlBase) {
    static final List<Channel> channels;

    static {
        try {
            Path path = Paths.get("channels.properties");
            if (Files.notExists(path)) {
                throw new IllegalStateException(path + " does not exists");
            }

            Properties properties = new Properties();
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                properties.load(reader);
            }

            String[] names = properties.getProperty("names").split(",");

            ArrayList<Channel> cs = new ArrayList<>();
            for (String name : names) {
                String urlBase = properties.getProperty(name + ".ci.url");
                if (urlBase == null) {
                    urlBase = "https://ci.huangyuhui.net/job/HMCL-" + name;
                }

                String chineseName = properties.getProperty(name + ".name.chinese");

                cs.add(new Channel(name, chineseName, urlBase));
            }
            channels = cs;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static Channel find(String name) {
        Objects.requireNonNull(name, "channel name is null");
        for (Channel channel : channels) {
            if (name.equals(channel.name)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Bad channel name: " + name);
    }

    public String artifactId() {
        return "hmcl-" + name;
    }

    public Optional<String> fetchVersion() throws IOException, ParserConfigurationException, SAXException {
        URL metadata = new URL("https://repo1.maven.org/maven2/org/glavo/hmcl/%s/maven-metadata.xml".formatted(artifactId()));
        System.out.println("Fetch maven metadata from " + metadata);

        HttpURLConnection connection = (HttpURLConnection) metadata.openConnection();
        int responseCode = connection.getResponseCode();

        if (responseCode == 404) {
            System.out.println("Maven repo does not exist temporarily, ignored");
            return Optional.empty();
        }

        if (responseCode != 200) {
            System.err.println("Failed to get metadata, response code: " + responseCode);
            try (InputStream errorStream = connection.getErrorStream()) {
                if (errorStream != null) {
                    errorStream.transferTo(System.err);
                }
            }
            return Optional.empty();
        }

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document;
        try (InputStream input = connection.getInputStream()) {
            document = builder.parse(input);
        }

        String latestVersion = document.getElementsByTagName("latest").item(0).getFirstChild().getNodeValue();
        return Optional.of(latestVersion);
    }

    public static Map<Channel, String> fetchVersions(boolean exitOnFailed) throws IOException, ParserConfigurationException, SAXException {
        var res = new LinkedHashMap<Channel, String>();
        for (Channel channel : channels) {
            Optional<String> version = channel.fetchVersion();
            version.ifPresentOrElse(
                    v -> res.put(channel, v),
                    () -> {
                        System.err.println("未能获取 %s 版本信息");
                        if (exitOnFailed) {
                            System.exit(-1);
                        }
                    }
            );
        }
        return res;
    }
}