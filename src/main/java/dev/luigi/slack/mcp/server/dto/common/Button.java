package dev.luigi.slack.mcp.server.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Button {
    private TextObject text;
    private String actionId;
    private String value;
    private String url;
}