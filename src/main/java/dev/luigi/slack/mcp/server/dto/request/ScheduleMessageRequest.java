package dev.luigi.slack.mcp.server.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleMessageRequest {
    private String channel;
    private String text;
    private Long postAt;
}
