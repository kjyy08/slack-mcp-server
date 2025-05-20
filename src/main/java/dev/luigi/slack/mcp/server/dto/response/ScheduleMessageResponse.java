package dev.luigi.slack.mcp.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleMessageResponse extends SlackResponse {
    private String scheduledMessageId;
    private Long postAt;
}