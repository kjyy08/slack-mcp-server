package dev.luigi.slack.mcp.server.service.history;

import com.slack.api.bolt.App;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import dev.luigi.slack.mcp.server.dto.common.Message;
import dev.luigi.slack.mcp.server.dto.request.ChannelHistoryRequest;
import dev.luigi.slack.mcp.server.dto.response.ChannelHistoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackChannelHistoryService implements ChannelHistoryService {
    private final App app;

    @Override
    public ChannelHistoryResponse fetchChannelHistory(ChannelHistoryRequest req) {
        try {
            ConversationsHistoryResponse conversationsHistoryResponse = app.client().conversationsHistory(r -> r
                    .channel(req.getChannel())
                    .limit(req.getLimit())
                    .cursor(req.getCursor())
            );

            return convertToFetchHistoryResponse(conversationsHistoryResponse);
        } catch (Exception e) {
            log.error("Failed to fetch channel history: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch channel history", e);
        }
    }

    private ChannelHistoryResponse convertToFetchHistoryResponse(ConversationsHistoryResponse conversationsHistoryResponse) {
        List<Message> messages = convertToMessageList(conversationsHistoryResponse.getMessages());

        return ChannelHistoryResponse.builder()
                .ok(conversationsHistoryResponse.isOk())
                .error(conversationsHistoryResponse.getError())
                .messages(messages)
                .nextCursor(conversationsHistoryResponse.getResponseMetadata().getNextCursor())
                .build();
    }

    private List<Message> convertToMessageList(List<com.slack.api.model.Message> messages) {
        return messages.stream()
                .map(message -> Message.builder()
                        .text(message.getText())
                        .build())
                .toList();
    }
}
