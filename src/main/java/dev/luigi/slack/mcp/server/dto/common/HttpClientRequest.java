package dev.luigi.slack.mcp.server.dto.common;

import lombok.Builder;
import org.springframework.http.HttpMethod;

@Builder
public record HttpClientRequest(String url, HttpMethod method, Object requestBody, String... headers) {
}
