package dev.luigi.slack.mcp.server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.luigi.slack.mcp.server.dto.common.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelHistoryResponse extends SlackResponse {
    private List<Message> messages;
    private String nextCursor;
}
