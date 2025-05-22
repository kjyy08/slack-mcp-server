package dev.luigi.slack.mcp.server.service.history;

import dev.luigi.slack.mcp.server.dto.request.ChannelHistoryRequest;
import dev.luigi.slack.mcp.server.dto.response.ChannelHistoryResponse;

public interface ChannelHistoryService {
    ChannelHistoryResponse fetchChannelHistory(ChannelHistoryRequest req);

}
