package dev.luigi.slack.mcp.server.dto.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Context {
    private List<TextObject> elements;
}
