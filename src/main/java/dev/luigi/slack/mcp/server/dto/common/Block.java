package dev.luigi.slack.mcp.server.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Block {
    private String type;
    private Section section;
    private Divider divider;
    private Actions actions;
    private Image image;
    private Context context;
    private TextObject text;
    private Header header;
}