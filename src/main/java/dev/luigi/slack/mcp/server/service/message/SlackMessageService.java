package dev.luigi.slack.mcp.server.service.message;

import com.slack.api.bolt.App;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import dev.luigi.slack.mcp.server.dto.request.PostMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.PostMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackMessageService implements MessageService {
    private final App app;

    @Override
    public PostMessageResponse postMessage(PostMessageRequest req) {
        try {
            ChatPostMessageResponse chatPostMessageResponse = app.client().chatPostMessage(r -> r
                    .text(req.getText())
                    .channel(req.getChannel()));

            return convertToPostMessageResponse(chatPostMessageResponse);
        } catch (Exception e) {
            log.error("Failed to send message to Slack: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send message to Slack", e);
        }
    }

    private PostMessageResponse convertToPostMessageResponse(ChatPostMessageResponse chatPostMessageResponse) {
        return PostMessageResponse.builder()
                .ok(chatPostMessageResponse.isOk())
                .error(chatPostMessageResponse.getError())
                .ts(chatPostMessageResponse.getTs())
                .channel(chatPostMessageResponse.getChannel())
                .build();
    }
}
