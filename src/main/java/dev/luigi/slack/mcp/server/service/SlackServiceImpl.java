package dev.luigi.slack.mcp.server.service;

import dev.luigi.slack.mcp.server.dto.request.*;
import dev.luigi.slack.mcp.server.dto.response.*;
import dev.luigi.slack.mcp.server.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackServiceImpl implements SlackService {
    private final MessageRepository messageRepository;
    private final ScheduleRepository scheduleRepository;
    private final FileRepository fileRepository;
    private final ReactionRepository reactionRepository;
    private final ChannelHistoryRepository channelHistoryRepository;

    @Value("${slack.slack-channel-id}")
    private String channelId;

    @Tool(name = "postMessage", description = """
            슬랙으로 메시지를 전송합니다
            """)
    @Override
    public PostMessageResponse postMessage(String text) {
        PostMessageRequest postMessageRequest = PostMessageRequest.builder()
                .channel(channelId)
                .text(text)
                .build();
        log.info("sendMessage: {}", postMessageRequest);
        return messageRepository.postMessage(postMessageRequest);
    }

    //    @Tool(name = "fetchHistory", description = """
//
//            """)
    @Override
    public FetchHistoryResponse fetchChannelHistory(FetchHistoryRequest req) {
        return channelHistoryRepository.fetchChannelHistory(req);
    }

    //    @Tool(name = "uploadFile", description = """
//
//            """)
    @Override
    public UploadFileResponse uploadFile(UploadFileRequest req) {
        return fileRepository.uploadFile(req);
    }

    //    @Tool(name = "addReaction", description = """
//
//            """)
    @Override
    public ReactionResponse addReaction(ReactionRequest req) {
        return reactionRepository.addReaction(req);
    }

    //    @Tool(name = "removeReaction", description = """
//
//            """)
    @Override
    public ReactionResponse removeReaction(ReactionRequest req) {
        return reactionRepository.removeReaction(req);
    }

    //    @Tool(name = "scheduleMessage", description = """
//
//            """)
    @Override
    public ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req) {
        return scheduleRepository.scheduleMessage(req);
    }
}
