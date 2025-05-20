package dev.luigi.slack.mcp.server.dto.response;

import dev.luigi.slack.mcp.server.dto.common.Message;

import java.util.List;

public class FetchHistoryResponse extends SlackResponse {
    private List<Message> messages;
    private String nextCursor;

    public FetchHistoryResponse(boolean ok, String error, String channel, String ts) {
        super(ok, error, channel, ts);
    }
}
