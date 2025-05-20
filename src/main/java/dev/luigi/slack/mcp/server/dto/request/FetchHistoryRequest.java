package dev.luigi.slack.mcp.server.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchHistoryRequest {
    private String channel;
    private Integer limit;
    private String cursor;
}
