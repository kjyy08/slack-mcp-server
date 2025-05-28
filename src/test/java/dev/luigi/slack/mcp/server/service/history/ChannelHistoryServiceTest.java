package dev.luigi.slack.mcp.server.service.history;

import com.slack.api.RequestConfigurator;
import com.slack.api.bolt.App;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Message;
import com.slack.api.model.ResponseMetadata;
import dev.luigi.slack.mcp.server.dto.request.ChannelHistoryRequest;
import dev.luigi.slack.mcp.server.dto.response.ChannelHistoryResponse;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChannelHistoryServiceTest {
    @Mock
    private App mockApp;

    @Mock
    private MethodsClient mockClient;

    @InjectMocks
    private SlackChannelHistoryService slackChannelHistoryService;

    private String channel;
    private int limit;
    private String cursor;

    @BeforeEach
    void setUp() {
        channel = "C1234567890";
        limit = 25;
        cursor = null;
    }

    @Test
    @DisplayName("채널 히스토리 조회가 정상적으로 수행되어야 한다.")
    void fetchChannelHistorySucceeds() throws SlackApiException, IOException {
        // Given
        Message mockMessage = mock(Message.class);
        when(mockMessage.getText()).thenReturn("Test message");

        ResponseMetadata mockResponseMetadata = mock(ResponseMetadata.class);
        when(mockResponseMetadata.getNextCursor()).thenReturn("next_cursor_value");

        ConversationsHistoryResponse resp = new ConversationsHistoryResponse();
        resp.setOk(true);
        resp.setMessages(List.of(mockMessage));
        resp.setResponseMetadata(mockResponseMetadata);
        resp.setError(null);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.conversationsHistory(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        ChannelHistoryResponse result = slackChannelHistoryService.fetchChannelHistory(ChannelHistoryRequest.builder()
                .channel(channel)
                .limit(limit)
                .cursor(cursor)
                .build());

        // Then
        assertThat(result.isOk(), is(true));
        assertThat(result.getMessages().size(), is(1));
        assertThat(result.getMessages().get(0).getText(), is("Test message"));
        assertThat(result.getNextCursor(), is("next_cursor_value"));
        assertThat(result.getError(), is((String) null));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).conversationsHistory(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("채널 히스토리 조회 실패 시 적절한 에러 응답을 반환해야 한다.")
    void fetchChannelHistoryFails() throws SlackApiException, IOException {
        // Given
        ResponseMetadata mockResponseMetadata = mock(ResponseMetadata.class);
        when(mockResponseMetadata.getNextCursor()).thenReturn("");

        ConversationsHistoryResponse resp = new ConversationsHistoryResponse();
        resp.setOk(false);
        resp.setMessages(List.of());
        resp.setResponseMetadata(mockResponseMetadata);
        resp.setError("channel_not_found");

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.conversationsHistory(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        ChannelHistoryResponse result = slackChannelHistoryService.fetchChannelHistory(ChannelHistoryRequest.builder()
                .channel(channel)
                .limit(limit)
                .cursor(cursor)
                .build());

        // Then
        assertThat(result.isOk(), is(false));
        assertThat(result.getMessages(), is(List.of()));
        assertThat(result.getNextCursor(), is(""));
        assertThat(result.getError(), is("channel_not_found"));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).conversationsHistory(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("채널 히스토리 조회 중 SlackApiException 발생 시 적절히 처리되어야 한다.")
    void fetchChannelHistoryThrowsSlackApiException() throws SlackApiException, IOException {
        // given
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder()
                        .url("https://slack.com/api/conversations.history")
                        .build())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("Bad Request")
                .build();

        String errorResponseBody = "{\"ok\":false,\"error\":\"Slack API error\"}";

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.conversationsHistory(any(RequestConfigurator.class))).thenThrow(new SlackApiException(mockResponse, errorResponseBody));

        // when & then
        assertThatThrownBy(() -> slackChannelHistoryService.fetchChannelHistory(ChannelHistoryRequest.builder()
                .channel(channel)
                .limit(limit)
                .cursor(cursor)
                .build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch channel history");
    }

    @Test
    @DisplayName("채널 히스토리 조회 중 IO 예외 발생 시 적절히 처리되어야 한다.")
    void fetchChannelHistoryThrowsIOException() throws SlackApiException, IOException {
        // given
        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.conversationsHistory(any(RequestConfigurator.class))).thenThrow(new IOException("Network error"));

        // when & then
        assertThatThrownBy(() -> slackChannelHistoryService.fetchChannelHistory(ChannelHistoryRequest.builder()
                .channel(channel)
                .limit(limit)
                .cursor(cursor)
                .build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch channel history");
    }
}