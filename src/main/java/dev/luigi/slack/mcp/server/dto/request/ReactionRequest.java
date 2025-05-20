package dev.luigi.slack.mcp.server.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionRequest {
    private String name;
    private String channel;
    private String timestamp;
}
