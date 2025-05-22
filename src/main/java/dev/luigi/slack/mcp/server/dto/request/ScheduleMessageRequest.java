package dev.luigi.slack.mcp.server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleMessageRequest {
    private String channel;
    private String text;
    private Long postAt;
}
