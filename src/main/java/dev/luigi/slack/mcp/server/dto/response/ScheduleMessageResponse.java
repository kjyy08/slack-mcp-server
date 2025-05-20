package dev.luigi.slack.mcp.server.dto.response;

public class ScheduleMessageResponse extends SlackResponse {
    private String scheduledMessageId;
    private Long postAt;

    public ScheduleMessageResponse(boolean ok, String error, String channel, String ts) {
        super(ok, error, channel, ts);
    }
}