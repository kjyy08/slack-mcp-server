package dev.luigi.slack.mcp.server.dto.response;

import dev.luigi.slack.mcp.server.dto.common.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FetchHistoryResponse extends SlackResponse {
    private List<Message> messages;
    private String nextCursor;
}
