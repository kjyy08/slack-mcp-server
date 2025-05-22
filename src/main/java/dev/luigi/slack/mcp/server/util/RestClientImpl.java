package dev.luigi.slack.mcp.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.common.HttpClientRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.File;

@Component
@Slf4j
@RequiredArgsConstructor
public class RestClientImpl implements CustomHttpClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Override
    public String sendRequest(HttpClientRequest httpClientRequest) throws RuntimeException {
        try {
            HttpHeaders headers = convertToHttpHeaders(httpClientRequest.headers());
            String jsonBody = objectMapper.writeValueAsString(httpClientRequest.requestBody());
            log.info("Sending request to {} with headers: {} and body: {}", httpClientRequest.url(), headers, jsonBody);

            String response = restClient.method(httpClientRequest.method())
                    .uri(httpClientRequest.url())
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);

            log.info("Received response from {}: {}", httpClientRequest.url(), response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while sending the request", e);
        }
    }

    @Override
    public String sendMultipartRequest(HttpClientRequest httpClientRequest) throws RuntimeException {
        try {
            HttpHeaders headers = convertToHttpHeaders(httpClientRequest.headers());

            if (!isMultipartRequest(headers)) {
                throw new IllegalArgumentException("Headers must contain Content-Type: multipart/form-data");
            }

            MultiValueMap<String, Object> body = createMultipartRequestBody(httpClientRequest);
            log.info("Sending multipart request to {} with headers: {} and body: {}", httpClientRequest.url(), headers, body);

            String response = restClient.method(httpClientRequest.method())
                    .uri(httpClientRequest.url())
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(body)
                    .retrieve()
                    .body(String.class);

            log.info("Received multipart response from {}: {}", httpClientRequest.url(), response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while sending the multipart request", e);
        }
    }

    private HttpHeaders convertToHttpHeaders(String... headers) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (headers != null) {
            if (headers.length % 2 != 0) {
                throw new IllegalArgumentException("Headers must be key-value pairs.");
            }

            for (int i = 0; i < headers.length; i += 2) {
                String key = headers[i];
                String value = headers[i + 1];
                httpHeaders.add(key, value);
            }
        } else {
            log.warn("No headers were provided");
            throw new IllegalArgumentException("Headers cannot be null");
        }

        return httpHeaders;
    }

    private boolean isMultipartRequest(HttpHeaders httpHeaders) {
        return httpHeaders.getContentType() != null &&
                httpHeaders.getContentType().isCompatibleWith(MediaType.MULTIPART_FORM_DATA);
    }

    private MultiValueMap<String, Object> createMultipartRequestBody(HttpClientRequest httpClientRequest) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource((File) httpClientRequest.requestBody()));
        return body;
    }
}
