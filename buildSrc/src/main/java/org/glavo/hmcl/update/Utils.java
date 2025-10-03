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

import com.google.gson.Gson;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class Utils {
    private static final Logger LOGGER = Logging.getLogger(Utils.class);

    static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    static final Gson GSON = new Gson();

    static <T> T fetch(URI uri, HttpResponse.BodyHandler<T> handler) throws IOException, InterruptedException {
        LOGGER.info("Fetching {}", uri);

        HttpResponse<T> response = HTTP_CLIENT.send(HttpRequest.newBuilder(uri).build(), handler);
        if (response.statusCode() / 100 != 2) {
            throw new IOException("Bad status code " + response.statusCode() + " for " + uri);
        }

        return response.body();
    }

    static <T> T fetch(URI uri, Class<T> type) throws IOException, InterruptedException {
        return GSON.fromJson(fetch(uri, HttpResponse.BodyHandlers.ofString()), type);
    }
}
