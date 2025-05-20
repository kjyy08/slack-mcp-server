package dev.luigi.slack.mcp.server.service.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.common.HttpClientRequest;
import dev.luigi.slack.mcp.server.dto.request.FetchHistoryRequest;
import dev.luigi.slack.mcp.server.dto.response.FetchHistoryResponse;
import dev.luigi.slack.mcp.server.service.common.AbstractSlackService;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class ChannelHistoryServiceImpl extends AbstractSlackService implements ChannelHistoryService {
    private final String url = "https://slack.com/api/conversations.history";

    public ChannelHistoryServiceImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public FetchHistoryResponse fetchChannelHistory(FetchHistoryRequest req) {
        try {
            String[] headers = {
                    "Authorization", "Bearer " + token,
            };

            String requestUrl = UriComponentsBuilder.fromUriString(url)
                    .queryParam("channel", req.getChannel())
                    .queryParam("limit", req.getLimit())
                    .queryParam("cursor", req.getCursor())
                    .build()
                    .toUriString();

            HttpClientRequest httpRequest = HttpClientRequest.builder()
                    .url(requestUrl)
                    .method(HttpMethod.GET)
                    .headers(headers)
                    .build();

            String responseBody = httpClient.sendRequest(httpRequest);
            return objectMapper.readValue(responseBody, FetchHistoryResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch channel history: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch channel history", e);
        }
    }
}
