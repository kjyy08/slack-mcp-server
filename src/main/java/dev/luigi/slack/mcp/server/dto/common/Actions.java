package dev.luigi.slack.mcp.server.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Actions {
    private List<Element> elements;
}
