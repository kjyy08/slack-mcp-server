package dev.luigi.slack.mcp.server.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Context {
    private List<TextObject> elements;
}
