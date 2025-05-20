package dev.luigi.slack.mcp.server.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextObject {
    private String type;
    private String text;
    private Boolean emoji;
}
