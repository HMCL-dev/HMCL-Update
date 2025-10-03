
/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2025 huangyuhui <huanghongxun2008@126.com> and contributors
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

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.plugins.ExtraPropertiesExtension;

import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// @author Glavo
public final class CheckUpdate {
    public static final String HMCL_VERSION = "HMCL_VERSION";
    public static final String HMCL_DOWNLOAD_BASE_URI = "HMCL_DOWNLOAD_BASE_URI";

    private static final Logger LOGGER = Logging.getLogger(CheckUpdate.class);

    private static final String WORKFLOW_JOB = "org.jenkinsci.plugins.workflow.job.WorkflowJob";
    private static final String WORKFLOW_MULTI_BRANCH_PROJECT = "org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject";
    private static final String FREE_STYLE_PROJECT = "hudson.model.FreeStyleProject";

    private static String concatUri(String base, String... others) {
        var builder = new StringBuilder(base);
        for (String other : others) {
            if (builder.charAt(builder.length() - 1) != '/') {
                builder.append('/');
            }
            builder.append(Objects.requireNonNull(other));
        }
        return builder.toString();
    }

    public static void apply(Project project, UpdateChannel channel) throws Exception {
        LOGGER.quiet("CHANNEL: {}", channel);

        URI apiUri = URI.create(concatUri(channel.getUri(), "api", "json"));

        BuildMetadata buildMetadata;

        JsonObject body = Objects.requireNonNull(Utils.fetch(apiUri, JsonObject.class));
        String jobType = Objects.requireNonNull(body.getAsJsonPrimitive("_class"), "Missing _class property")
                .getAsString();

        if (WORKFLOW_MULTI_BRANCH_PROJECT.equals(jobType)) {
            Pattern namePattern = Pattern.compile("release%2F3\\.\\d+");

            List<BuildMetadata> metadatas = Objects.requireNonNull(Utils.GSON.fromJson(body.get("jobs"), new TypeToken<List<SubJobInfo>>() {
                    }), "jobs")
                    .stream()
                    .filter(it -> WORKFLOW_JOB.equals(it._class()))
                    .filter(it -> namePattern.matcher(it.name()).matches())
                    .filter(it -> !it.color().equals("disabled"))
                    .map(it -> {
                        try {
                            return fetchBuildInfo(it.url);
                        } catch (Throwable e) {
                            throw new GradleException("Failed to fetch build info from " + it.url(), e);
                        }
                    }).sorted(Comparator.comparing(BuildMetadata::timestamp))
                    .toList();

            if (metadatas.isEmpty())
                throw new GradleException("Failed to fetch build metadata from " + apiUri);

            buildMetadata = metadatas.getLast();
        } else if (WORKFLOW_JOB.equals(jobType) || FREE_STYLE_PROJECT.equals(jobType)) {
            buildMetadata = fetchBuildInfo(channel.getUri());
        } else {
            throw new GradleException("Unsupported job type: " + jobType);
        }

        LOGGER.quiet("Build metadata found: {}", buildMetadata);

        ExtraPropertiesExtension ext = project.getExtensions().getExtraProperties();
        ext.set(HMCL_VERSION, buildMetadata.version);
        ext.set(HMCL_DOWNLOAD_BASE_URI, buildMetadata.downloadBaseUri);
    }

    private static BuildMetadata fetchBuildInfo(String jobUri) throws IOException, InterruptedException {
        URI uri = URI.create(concatUri(jobUri, "lastSuccessfulBuild", "api", "json"));

        LOGGER.quiet("Fetching build info from {}", uri);

        BuildInfo buildInfo = Objects.requireNonNull(Utils.fetch(uri, BuildInfo.class), "build info");

        String revision = Objects.requireNonNullElse(buildInfo.actions(), List.<ActionInfo>of())
                .stream()
                .filter(action -> "hudson.plugins.git.util.BuildData".equals(action._class()))
                .map(ActionInfo::lastBuiltRevision)
                .map(BuiltRevision::SHA1)
                .findFirst()
                .orElseThrow(() -> new GradleException("Could not find revision"));
        if (!revision.matches("[0-9a-z]{40}"))
            throw new GradleException("Invalid revision: " + revision);

        Pattern fileNamePattern = Pattern.compile("HMCL-(?<version>\\d+(?:\\.\\d+)+)\\.jar");
        ArtifactInfo jarArtifact = Objects.requireNonNullElse(buildInfo.artifacts(), List.<ArtifactInfo>of()).stream()
                .filter(it -> fileNamePattern.matcher(it.fileName()).matches())
                .findFirst()
                .orElseThrow(() -> new GradleException("Could not find .jar artifact"));

        String fileName = jarArtifact.fileName();
        String relativePath = jarArtifact.relativePath();
        if (!relativePath.endsWith("/" + fileName)) {
            throw new GradleException("Invalid artifact relative path: " + jarArtifact);
        }

        Matcher matcher = fileNamePattern.matcher(fileName);
        if (!matcher.matches()) {
            throw new AssertionError("Artifact: " + jarArtifact.fileName());
        }

        String version = matcher.group("version");

        String downloadBaseUrl = concatUri(buildInfo.url(), "artifact",
                relativePath.substring(0, relativePath.length() - fileName.length() - 1));

        return new BuildMetadata(version, revision, buildInfo.timestamp(), downloadBaseUrl);
    }

    record BuildMetadata(String version, String revision, long timestamp, String downloadBaseUri) {
    }

    private record SubJobInfo(String _class, String name, String url, String color) {

    }

    private record BuildInfo(String url,
                             long number,
                             long timestamp,
                             List<ArtifactInfo> artifacts,
                             List<ActionInfo> actions
    ) {
    }

    record ArtifactInfo(String fileName, String relativePath) {
    }

    record ActionInfo(String _class, BuiltRevision lastBuiltRevision) {
    }

    record BuiltRevision(String SHA1) {
    }
}
