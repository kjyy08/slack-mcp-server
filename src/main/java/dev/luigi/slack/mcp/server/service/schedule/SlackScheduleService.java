package dev.luigi.slack.mcp.server.service.schedule;

import com.slack.api.bolt.App;
import com.slack.api.methods.response.chat.ChatScheduleMessageResponse;
import com.slack.api.methods.response.chat.ChatScheduleMessageResponse.ScheduledMessage;
import dev.luigi.slack.mcp.server.dto.common.Message;
import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.ScheduleMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackScheduleService implements ScheduleService {
    private final App app;

    @Override
    public ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req) {
        try {
            ChatScheduleMessageResponse chatScheduleMessageResponse = app.client().chatScheduleMessage(r -> r
                    .channel(req.getChannel())
                    .text(req.getText())
                    .postAt(req.getPostAt()));

            return convertToScheduleMessageResponse(chatScheduleMessageResponse);
        } catch (Exception e) {
            log.error("Failed to schedule message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to schedule message", e);
        }
    }

    private ScheduleMessageResponse convertToScheduleMessageResponse(ChatScheduleMessageResponse chatScheduleMessageResponse) {
        Message message = convertToMessage(chatScheduleMessageResponse.getMessage());

        return ScheduleMessageResponse.builder()
                .ok(chatScheduleMessageResponse.isOk())
                .error(chatScheduleMessageResponse.getError())
                .channel(chatScheduleMessageResponse.getChannel())
                .message(message)
                .postAt(chatScheduleMessageResponse.getPostAt())
                .build();
    }

    private Message convertToMessage(ScheduledMessage message) {
        return Message.builder()
                .text(message.getText())
                .build();
    }
}
