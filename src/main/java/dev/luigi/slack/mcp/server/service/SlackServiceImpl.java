package dev.luigi.slack.mcp.server.service;

import dev.luigi.slack.mcp.server.dto.common.Block;
import dev.luigi.slack.mcp.server.dto.common.TextObject;
import dev.luigi.slack.mcp.server.dto.request.*;
import dev.luigi.slack.mcp.server.dto.response.*;
import dev.luigi.slack.mcp.server.service.file.FileService;
import dev.luigi.slack.mcp.server.service.history.ChannelHistoryService;
import dev.luigi.slack.mcp.server.service.message.MessageService;
import dev.luigi.slack.mcp.server.service.reaction.ReactionService;
import dev.luigi.slack.mcp.server.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService {
    private final MessageService messageService;
    private final ScheduleService scheduleService;
    private final FileService fileService;
    private final ReactionService reactionService;
    private final ChannelHistoryService channelHistoryService;

    @Value("${slack.slack-channel-id}")
    private String channelId;

    @Tool(name = "postMessage", description = """
            슬랙 메시지를 전송합니다
            """)
    @Override
    public PostMessageResponse postMessage(
            @ToolParam(description = """
                    기존의 마크다운 문법 대신 다음 형식의 슬랙 전용 마크다운 문법을 지켜서 입력하세요:
                    
                    - *굵게* (❌ **굵게** 사용 금지)
                    - _기울임_ (❌ __기울임__ 사용 금지)
                    - `인라인 코드`
                    - ```여러 줄 코드```
                    - > 인용
                    - - 리스트, 1. 번호 목록
                    - <http://url|링크 텍스트>
                    - 줄바꿈은 두 줄 사이 공백 또는 \\n 사용
                    
                    ❗ 표, 헤더(# 제목), 이미지, HTML 태그는 지원되지 않습니다.
                    """) String text
    ) {
        TextObject textObject = TextObject.builder()
                .type("mrkdwn")
                .text(text)
                .build();

        Block block = Block.builder()
                .type("section")
                .text(textObject)
                .build();

        PostMessageRequest postMessageRequest = PostMessageRequest.builder()
                .channel(channelId)
                .blocks(List.of(block))
                .build();

        return messageService.postMessage(postMessageRequest);
    }

    @Tool(name = "fetchChannelHistory", description = """
            슬랙 채널의 메시지 내역을 조회합니다
            """)
    @Override
    public FetchHistoryResponse fetchChannelHistory(
            @ToolParam(required = false, description = "조회 메시지 수의 최대값")
            @RequestParam(defaultValue = "50") int limit,
            @ToolParam(description = "페이징 처리를 위한 커서, 이전 요청의 응답에서 받은 커서를 사용해 다음 페이지를 조회")
            String cursor
    ) {
        FetchHistoryRequest fetchHistoryRequest = FetchHistoryRequest.builder()
                .channel(channelId)
                .limit(limit)
                .cursor(cursor)
                .build();

        return channelHistoryService.fetchChannelHistory(fetchHistoryRequest);
    }

    @Override
    public UploadFileResponse uploadFile(UploadFileRequest req) {
        return fileService.uploadFile(req);
    }

    @Override
    public ReactionResponse addReaction(ReactionRequest req) {
        return reactionService.addReaction(req);
    }

    @Override
    public ReactionResponse removeReaction(ReactionRequest req) {
        return reactionService.removeReaction(req);
    }

    @Override
    public ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req) {
        return scheduleService.scheduleMessage(req);
    }
}
