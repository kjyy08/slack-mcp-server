package dev.luigi.slack.mcp.server.service.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.request.FetchHistoryRequest;
import dev.luigi.slack.mcp.server.dto.response.FetchHistoryResponse;
import dev.luigi.slack.mcp.server.service.common.AbstractSlackService;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import org.springframework.stereotype.Service;

@Service
public class ChannelHistoryServiceImpl extends AbstractSlackService implements ChannelHistoryService {

    public ChannelHistoryServiceImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public FetchHistoryResponse fetchChannelHistory(FetchHistoryRequest req) {
        return null;
    }
}
