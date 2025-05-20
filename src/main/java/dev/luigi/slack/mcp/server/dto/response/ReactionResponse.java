package dev.luigi.slack.mcp.server.dto.response;

public class ReactionResponse extends SlackResponse {
    public ReactionResponse(boolean ok, String error, String channel, String ts) {
        super(ok, error, channel, ts);
    }
}
