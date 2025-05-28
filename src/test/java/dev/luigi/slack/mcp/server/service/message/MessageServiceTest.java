package dev.luigi.slack.mcp.server.service.message;

import com.slack.api.RequestConfigurator;
import com.slack.api.bolt.App;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import dev.luigi.slack.mcp.server.dto.request.PostMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.PostMessageResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    private App mockApp;

    @Mock
    private MethodsClient mockClient;

    @InjectMocks
    private SlackMessageService slackMessageService;

    private String channel;
    private String text;

    @BeforeEach
    void setUp() {
        channel = "C1234567890";
        text = "Hello, Slack!";
    }

    @Test
    @DisplayName("슬랙 메시지가 정상적으로 전송이 되어야 한다.")
    void postMessageSucceeds() throws SlackApiException, IOException {
        // Given
        ChatPostMessageResponse resp = new ChatPostMessageResponse();
        resp.setOk(true);
        resp.setTs("1234567890.123456");
        resp.setChannel(channel);
        resp.setError(null);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.chatPostMessage(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        PostMessageResponse result = slackMessageService.postMessage(PostMessageRequest.builder()
                .channel(channel)
                .text(text)
                .build());

        // Then
        assertThat(result.isOk(), is(true));
        assertThat(result.getTs(), is("1234567890.123456"));
        assertThat(result.getChannel(), is("C1234567890"));
        assertThat(result.getError(), is((String) null));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).chatPostMessage(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("슬랙 메시지 전송에 실패하면 적절한 에러 응답을 반환해야 한다.")
    void postMessageFails() throws SlackApiException, IOException {
        // Given
        ChatPostMessageResponse resp = new ChatPostMessageResponse();
        resp.setOk(false);
        resp.setError("invalid_channel");
        resp.setTs(null);
        resp.setChannel(null);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.chatPostMessage(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        PostMessageResponse result = slackMessageService.postMessage(PostMessageRequest.builder()
                .channel(channel)
                .text(text)
                .build());

        // Then
        assertThat(result.isOk(), is(false));
        assertThat(result.getError(), is("invalid_channel"));
        assertThat(result.getTs(), is((String) null));
        assertThat(result.getChannel(), is((String) null));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).chatPostMessage(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("슬랙 메시지 전송 중 Slack API 예외 발생 시 적절히 처리되어야 한다.")
    void postMessageThrowsSlackApiException() throws SlackApiException, IOException {
        // Given
        Response response = new Response.Builder()
                .request(new Request.Builder()
                        .url("https://slack.com/api/chat.postMessage")
                        .build())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("Bad Request")
                .build();

        String errorResponseBody = "{\"ok\":false,\"error\":\"invalid_auth\"}";

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.chatPostMessage(any(RequestConfigurator.class))).thenThrow(new SlackApiException(response, errorResponseBody));

        // When & Then
        assertThatThrownBy(() -> slackMessageService.postMessage(PostMessageRequest.builder()
                .channel(channel)
                .text(text)
                .build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to send message to Slack");
    }

    @Test
    @DisplayName("슬랙 메시지 전송 중 IO 예외 발생 시 적절히 처리되어야 한다.")
    void postMessageThrowsIOException() throws SlackApiException, IOException {
        // Given
        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.chatPostMessage(any(RequestConfigurator.class))).thenThrow(new IOException("Network error"));

        // When & Then
        assertThatThrownBy(() -> slackMessageService.postMessage(PostMessageRequest.builder()
                .channel(channel)
                .text(text)
                .build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to send message to Slack");
    }
}