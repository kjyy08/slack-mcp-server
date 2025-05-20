package dev.luigi.slack.mcp.server.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.tool.annotation.ToolParam;

@Getter
@Setter
public abstract class SlackResponse {
    @ToolParam(description = "슬랙 메시지 처리 성공 여부")
    protected boolean ok;
    @ToolParam(description = "오류 메시지 (ok=false일 경우")
    protected String error;
    @ToolParam(description = "참여한 채널 ID")
    protected String channel;
    @ToolParam(description = "메시지 Timestamp")
    protected String ts;
}
