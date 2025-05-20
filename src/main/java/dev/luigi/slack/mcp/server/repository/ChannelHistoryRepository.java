package dev.luigi.slack.mcp.server.repository;

import dev.luigi.slack.mcp.server.dto.request.FetchHistoryRequest;
import dev.luigi.slack.mcp.server.dto.response.FetchHistoryResponse;

public interface ChannelHistoryRepository {
    FetchHistoryResponse fetchChannelHistory(FetchHistoryRequest req);

}
