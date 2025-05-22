package dev.luigi.slack.mcp.server.util;

import dev.luigi.slack.mcp.server.dto.common.HttpClientRequest;

public interface CustomHttpClient {
    String sendRequest(HttpClientRequest httpClientRequest) throws RuntimeException;

    String sendMultipartRequest(HttpClientRequest httpClientRequest) throws RuntimeException;
}
