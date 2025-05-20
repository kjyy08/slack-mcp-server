package dev.luigi.slack.mcp.server.service.history;

import dev.luigi.slack.mcp.server.dto.request.FetchHistoryRequest;
import dev.luigi.slack.mcp.server.dto.response.FetchHistoryResponse;

public interface ChannelHistoryService {
    FetchHistoryResponse fetchChannelHistory(FetchHistoryRequest req);

}
