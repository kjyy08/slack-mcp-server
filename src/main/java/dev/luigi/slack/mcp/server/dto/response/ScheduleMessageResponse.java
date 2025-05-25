package dev.luigi.slack.mcp.server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.luigi.slack.mcp.server.dto.common.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleMessageResponse extends SlackResponse {
    private Message message;
    private int postAt;
}