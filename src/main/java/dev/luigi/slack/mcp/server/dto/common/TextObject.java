package dev.luigi.slack.mcp.server.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextObject {
    private String type;
    private String text;
    private Boolean emoji;
}
