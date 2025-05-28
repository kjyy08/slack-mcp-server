package dev.luigi.slack.mcp.server.service.schedule;

import com.slack.api.RequestConfigurator;
import com.slack.api.bolt.App;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatScheduleMessageResponse;
import com.slack.api.methods.response.chat.ChatScheduleMessageResponse.ScheduledMessage;
import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.ScheduleMessageResponse;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @Mock
    private App mockApp;

    @Mock
    private MethodsClient mockClient;

    @InjectMocks
    private SlackScheduleService slackScheduleService;

    private String channel;
    private String text;
    private int postAt;

    @BeforeEach
    void setUp() {
        channel = "C1234567890";
        text = "예약된 메시지입니다.";
        postAt = 60;
    }

    @Test
    @DisplayName("슬랙 메시지 예약 전송이 정상적으로 수행되어야 한다.")
    void scheduleMessageSucceeds() throws SlackApiException, IOException {
        // Given
        ScheduledMessage mockMessage = mock(ChatScheduleMessageResponse.ScheduledMessage.class);
        when(mockMessage.getText()).thenReturn(text);

        ChatScheduleMessageResponse resp = new ChatScheduleMessageResponse();
        resp.setOk(true);
        resp.setScheduledMessageId("Q1234567890");
        resp.setChannel(channel);
        resp.setPostAt((int) System.currentTimeMillis() / 1000 + postAt * 60);
        resp.setMessage(mockMessage);
        resp.setError(null);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.chatScheduleMessage(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        ScheduleMessageResponse result = slackScheduleService.scheduleMessage(ScheduleMessageRequest.builder()
                .channel(channel)
                .text(text)
                .postAt(postAt)
                .build());

        // Then
        assertThat(result.isOk(), is(true));
        assertThat(result.getMessage().getText(), is(text));
        assertThat(result.getError(), is(nullValue()));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).chatScheduleMessage(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("슬랙 메시지 예약 전송 실패 시 적절한 에러 응답을 반환해야 한다.")
    void scheduleMessageFails() throws SlackApiException, IOException {
        // Given
        ScheduledMessage mockMessage = mock(ChatScheduleMessageResponse.ScheduledMessage.class);
        when(mockMessage.getText()).thenReturn("");

        ChatScheduleMessageResponse resp = new ChatScheduleMessageResponse();
        resp.setOk(false);
        resp.setMessage(mockMessage);
        resp.setError("invalid_time");
        resp.setScheduledMessageId(null);
        resp.setChannel(null);
        resp.setPostAt(0);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.chatScheduleMessage(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        ScheduleMessageResponse result = slackScheduleService.scheduleMessage(ScheduleMessageRequest.builder()
                .channel(channel)
                .text(text)
                .postAt(postAt)
                .build());

        // Then
        assertThat(result.isOk(), is(false));
        assertThat(result.getError(), is("invalid_time"));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).chatScheduleMessage(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("슬랙 메시지 예약 전송 중 Slack API 예외 발생 시 적절히 처리되어야 한다.")
    void scheduleMessageThrowsSlackApiException() throws SlackApiException, IOException {
        // Given
        Response response = new Response.Builder()
                .request(new Request.Builder()
                        .url("https://slack.com/api/chat.scheduleMessage")
                        .build())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("Bad Request")
                .build();

        String errorResponseBody = "{\"ok\":false,\"error\":\"invalid_auth\"}";

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.chatScheduleMessage(any(RequestConfigurator.class))).thenThrow(new SlackApiException(response, errorResponseBody));

        // When & Then
        assertThatThrownBy(() -> slackScheduleService.scheduleMessage(ScheduleMessageRequest.builder()
                .channel(channel)
                .text(text)
                .postAt(postAt)
                .build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to schedule message");
    }

    @Test
    @DisplayName("슬랙 메시지 예약 전송 중 IO 예외 발생 시 적절히 처리되어야 한다.")
    void scheduleMessageThrowsIOException() throws SlackApiException, IOException {
        // Given
        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.chatScheduleMessage(any(RequestConfigurator.class))).thenThrow(new IOException("Network error"));

        // When & Then
        assertThatThrownBy(() -> slackScheduleService.scheduleMessage(ScheduleMessageRequest.builder()
                .channel(channel)
                .text(text)
                .postAt(postAt)
                .build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to schedule message");
    }
}