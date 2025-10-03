/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2025 Glavo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.glavo.hmcl.update;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

public abstract class CheckExisting extends DefaultTask {
    public static final String NEED_UPDATE = "HMCL_NEED_UPDATE";

    private static final Logger LOGGER = Logging.getLogger(CheckExisting.class);
    private static final Pattern PATTERN = Pattern.compile("^[0-9]+\\.[0-9]+\\.[0-9]+(\\.[0-9]+)?$");

    @Input
    public abstract Property<@NotNull UpdateChannel> getChannel();

    @Input
    public abstract Property<@NotNull String> getHMCLVersion();

    @TaskAction
    public void run() throws Exception {
        UpdateChannel channel = getChannel().get();

        String version = System.getenv("HMCL_VERSION");
        if (!PATTERN.matcher(version).matches()) {
            throw new IllegalArgumentException("Bad HMCL version: " + version);
        }

        Document document;
        try (InputStream body = Utils.fetch(
                URI.create("https://repo1.maven.org/maven2/org/glavo/hmcl/hmcl-%s/maven-metadata.xml".formatted(channel.getMavenArtifactId())),
                HttpResponse.BodyHandlers.ofInputStream()
        )) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(body);
        }

        NodeList versionList = document.getElementsByTagName("version");
        int length = versionList.getLength();

        for (int i = 0; i < length; i++) {
            Node item = versionList.item(i);
            if (version.equals(item.getFirstChild().getNodeValue())) {
                LOGGER.quiet("{} already exists, no update required", version);
                return;
            }
        }

        getProject().getExtensions().getExtraProperties().set(NEED_UPDATE, "true");
    }

}
