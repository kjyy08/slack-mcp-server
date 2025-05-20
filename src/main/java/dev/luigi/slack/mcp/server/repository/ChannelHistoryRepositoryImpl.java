package dev.luigi.slack.mcp.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.request.FetchHistoryRequest;
import dev.luigi.slack.mcp.server.dto.response.FetchHistoryResponse;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelHistoryRepositoryImpl extends AbstractSlackRepository implements ChannelHistoryRepository {

    public ChannelHistoryRepositoryImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public FetchHistoryResponse fetchChannelHistory(FetchHistoryRequest req) {
        return null;
    }
}
