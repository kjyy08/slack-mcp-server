package dev.luigi.slack.mcp.server.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Section {
    private TextObject text;
    private List<Field> fields;
}