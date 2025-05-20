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
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public PostMessageResponse postMessage(@ToolParam(description = "슬랙 요청 메시지, 마크다운 일부 지원") String text) {
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

        log.info("sendMessage: {}", postMessageRequest);
        return messageService.postMessage(postMessageRequest);
    }

    @Override
    public FetchHistoryResponse fetchChannelHistory(FetchHistoryRequest req) {
        return channelHistoryService.fetchChannelHistory(req);
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
