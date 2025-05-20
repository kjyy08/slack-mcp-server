package dev.luigi.slack.mcp.server.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
    private TextObject text;
    private Boolean emoji;
}
