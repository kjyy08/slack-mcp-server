package dev.luigi.slack.mcp.server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.ai.tool.annotation.ToolParam;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelHistoryRequest {
    @ToolParam(required = false, description = "조회할 슬랙 채널 ID")
    private String channel;
    @ToolParam(description = "조회 메시지 수의 최대값")
    private int limit;
    @ToolParam(description = "페이징 처리를 위한 커서, 이전 요청의 응답에서 받은 커서를 사용해 다음 페이지를 조회")
    private String cursor;
}
