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
            슬랙으로 메시지를 전송합니다
            """)
    @Override
    public PostMessageResponse postMessage(
            @ToolParam(description = "슬랙 요청 메시지, 마크다운 일부 지원") String text
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
