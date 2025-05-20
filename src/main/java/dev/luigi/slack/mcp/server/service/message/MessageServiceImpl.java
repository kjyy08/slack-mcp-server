package dev.luigi.slack.mcp.server.service.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.common.HttpClientRequest;
import dev.luigi.slack.mcp.server.dto.request.PostMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.PostMessageResponse;
import dev.luigi.slack.mcp.server.service.common.AbstractSlackService;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl extends AbstractSlackService implements MessageService {
    private final String url = "https://slack.com/api/chat.postMessage";

    public MessageServiceImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public PostMessageResponse postMessage(PostMessageRequest req) {
        try {
            String[] headers = {
                    "Authorization", "Bearer " + token,
                    "Content-Type", "application/json; charset=UTF-8"
            };

            HttpClientRequest httpRequest = HttpClientRequest.builder()
                    .url(url)
                    .method(HttpMethod.POST)
                    .headers(headers)
                    .requestBody(req)
                    .build();

            String responseBody = httpClient.sendRequest(httpRequest);
            return objectMapper.readValue(responseBody, PostMessageResponse.class);
        } catch (Exception e) {
            log.error("Failed to send message to Slack: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send message to Slack", e);
        }
    }
}
