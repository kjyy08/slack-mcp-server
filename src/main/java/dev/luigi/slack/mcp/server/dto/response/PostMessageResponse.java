package dev.luigi.slack.mcp.server.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMessageResponse extends SlackResponse {
    public PostMessageResponse(boolean ok, String error, String channel, String ts) {
        super(ok, error, channel, ts);
    }
}
