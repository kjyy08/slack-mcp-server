package dev.luigi.slack.mcp.server.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.tool.annotation.ToolParam;

@Getter
@Setter
@Builder
public class PostMessageRequest {
    @ToolParam(description = "메시지를 전송할 채널 ID")
    private String channel;
    @ToolParam(description = "메시지 내용")
    private String text;
    
//    @ToolParam(required = false, description = "Block Kit 메시지 블록")
//    private List<Block> blocks;
//    @ToolParam(required = false, description = "등록된 스마트 티넷 thread timestamp")
//    private String threadTs;
//    @ToolParam(required = false, description = "Disable URL auto-preview if false")
//    private Boolean unfurlLinks;
//    @ToolParam(required = false, description = "Disable media auto-preview if false")
//    private Boolean unfurlMedia;
//    @ToolParam(required = false, description = "메시지 보내는 사용자 이름")
//    private String username;
//    @ToolParam(required = false, description = "이모지 아이콘 e.g. \":robot_face:\"")
//    private String iconEmoji;
//    @ToolParam(required = false, description = "아이콘 URL")
//    private String iconUrl;
}
