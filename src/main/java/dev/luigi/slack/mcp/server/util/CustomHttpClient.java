package dev.luigi.slack.mcp.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.common.HttpClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class CustomHttpClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CustomHttpClient(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = objectMapper;
    }

    public String sendRequest(HttpClientRequest httpClientRequest) throws RuntimeException {
        try {
            String jsonBody = objectMapper.writeValueAsString(httpClientRequest.requestBody());
            HttpRequest request = buildHttpRequest(httpClientRequest, jsonBody);
            log.info("Sending request to {} with body {}", httpClientRequest.url(), jsonBody);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 300) {
                throw new RuntimeException(String.format("HTTP error (%d): %s", response.statusCode(), response.body()));
            }

            log.info("Received response from {}: {}", httpClientRequest.url(), response.body());
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while sending the request", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Sending the request was interrupted", e);
        }
    }

    private HttpRequest buildHttpRequest(HttpClientRequest httpClientRequest, String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(java.net.URI.create(httpClientRequest.url()))
                .method(httpClientRequest.method().name(), HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .headers(httpClientRequest.headers())
                .build();
    }
}
