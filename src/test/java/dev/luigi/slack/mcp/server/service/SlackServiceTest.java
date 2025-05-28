package dev.luigi.slack.mcp.server.service;

import dev.luigi.slack.mcp.server.dto.common.FileInfo;
import dev.luigi.slack.mcp.server.dto.common.Message;
import dev.luigi.slack.mcp.server.dto.request.ChannelHistoryRequest;
import dev.luigi.slack.mcp.server.dto.request.PostMessageRequest;
import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.request.UploadFileRequest;
import dev.luigi.slack.mcp.server.dto.response.ChannelHistoryResponse;
import dev.luigi.slack.mcp.server.dto.response.PostMessageResponse;
import dev.luigi.slack.mcp.server.dto.response.ScheduleMessageResponse;
import dev.luigi.slack.mcp.server.dto.response.UploadFileResponse;
import dev.luigi.slack.mcp.server.service.file.FileService;
import dev.luigi.slack.mcp.server.service.history.ChannelHistoryService;
import dev.luigi.slack.mcp.server.service.message.MessageService;
import dev.luigi.slack.mcp.server.service.schedule.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlackServiceTest {

    @Mock
    private MessageService messageService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private FileService fileService;

    @Mock
    private ChannelHistoryService channelHistoryService;

    @InjectMocks
    private SlackServiceImpl slackService;

    @Captor
    private ArgumentCaptor<PostMessageRequest> postMessageRequestCaptor;

    @Captor
    private ArgumentCaptor<ChannelHistoryRequest> channelHistoryRequestCaptor;

    @Captor
    private ArgumentCaptor<UploadFileRequest> uploadFileRequestCaptor;

    @Captor
    private ArgumentCaptor<ScheduleMessageRequest> scheduleMessageRequestCaptor;

    private String channelId;
    private String messageText;
    private String filePath;
    private String fileTitle;
    private String fileName;
    private String fileComment;
    private String base64Data;
    private int postAtMinutes;

    @BeforeEach
    void setUp() {
        channelId = "C1234567890";
        messageText = "테스트 메시지";
        filePath = "/tmp/test.txt";
        fileTitle = "테스트 파일";
        fileName = "test.txt";
        fileComment = "파일 설명";
        base64Data = "SGVsbG8gV29ybGQh"; // "Hello World!" in base64
        postAtMinutes = 60;

        // 채널 ID 설정
        ReflectionTestUtils.setField(slackService, "channelId", channelId);
    }

    @Test
    @DisplayName("메시지 전송 시 올바른 요청 객체가 MessageService로 전달되어야 한다")
    void postMessageShouldDelegateToMessageService() {
        // Given
        PostMessageResponse mockResponse = PostMessageResponse.builder()
                .ok(true)
                .channel(channelId)
                .ts("1234567890.123456")
                .build();

        when(messageService.postMessage(any(PostMessageRequest.class))).thenReturn(mockResponse);

        // When
        PostMessageResponse result = slackService.postMessage(messageText);

        // Then
        verify(messageService).postMessage(postMessageRequestCaptor.capture());
        PostMessageRequest capturedRequest = postMessageRequestCaptor.getValue();

        assertThat(capturedRequest.getChannel(), is(channelId));
        assertThat(capturedRequest.getText(), is(messageText));
        assertThat(result, is(mockResponse));
    }

    @Test
    @DisplayName("채널 히스토리 조회 시 올바른 요청 객체가 ChannelHistoryService로 전달되어야 한다")
    void channelHistoryShouldDelegateToChannelHistoryService() {
        // Given
        int limit = 25;
        String cursor = "nextCursor";

        List<Message> messages = List.of(Message.builder().text("메시지 1").build());

        ChannelHistoryResponse mockResponse = ChannelHistoryResponse.builder()
                .ok(true)
                .messages(messages)
                .nextCursor("newCursor")
                .build();

        when(channelHistoryService.fetchChannelHistory(any(ChannelHistoryRequest.class))).thenReturn(mockResponse);

        // When
        ChannelHistoryResponse result = slackService.channelHistory(limit, cursor);

        // Then
        verify(channelHistoryService).fetchChannelHistory(channelHistoryRequestCaptor.capture());
        ChannelHistoryRequest capturedRequest = channelHistoryRequestCaptor.getValue();

        assertThat(capturedRequest.getChannel(), is(channelId));
        assertThat(capturedRequest.getLimit(), is(limit));
        assertThat(capturedRequest.getCursor(), is(cursor));
        assertThat(result, is(mockResponse));
    }

    @Test
    @DisplayName("파일 업로드 시 올바른 요청 객체가 FileService로 전달되어야 한다")
    void uploadFileShouldDelegateToFileService() {
        // Given
        FileInfo fileInfo = FileInfo.builder()
                .title(fileTitle)
                .size(1024)
                .build();

        UploadFileResponse mockResponse = UploadFileResponse.builder()
                .ok(true)
                .file(fileInfo)
                .build();

        when(fileService.uploadFile(any(UploadFileRequest.class))).thenReturn(mockResponse);

        // When
        UploadFileResponse result = slackService.uploadFile(filePath, fileTitle, fileName, fileComment);

        // Then
        verify(fileService).uploadFile(uploadFileRequestCaptor.capture());
        UploadFileRequest capturedRequest = uploadFileRequestCaptor.getValue();

        assertThat(capturedRequest.getChannel(), is(channelId));
        assertThat(capturedRequest.getFile(), instanceOf(File.class));
        assertThat(capturedRequest.getFile().getPath(), is(filePath.replace("/", "\\")));
        assertThat(capturedRequest.getTitle(), is(fileTitle));
        assertThat(capturedRequest.getFilename(), is(fileName));
        assertThat(capturedRequest.getInitialComment(), is(fileComment));
        assertThat(result, is(mockResponse));
    }

    @Test
    @DisplayName("Base64 파일 업로드 시 올바른 요청 객체가 FileService로 전달되어야 한다")
    void uploadFileByBase64ShouldDelegateToFileService() {
        // Given
        FileInfo fileInfo = FileInfo.builder()
                .title(fileTitle)
                .size(1024)
                .build();

        UploadFileResponse mockResponse = UploadFileResponse.builder()
                .ok(true)
                .file(fileInfo)
                .build();

        when(fileService.uploadFileByBase64(any(UploadFileRequest.class))).thenReturn(mockResponse);

        // When
        UploadFileResponse result = slackService.uploadFileByBase64(base64Data, fileTitle, fileName, fileComment);

        // Then
        verify(fileService).uploadFileByBase64(uploadFileRequestCaptor.capture());
        UploadFileRequest capturedRequest = uploadFileRequestCaptor.getValue();

        assertThat(capturedRequest.getChannel(), is(channelId));
        assertThat(capturedRequest.getFileData(), is(Base64.getDecoder().decode(base64Data)));
        assertThat(capturedRequest.getTitle(), is(fileTitle));
        assertThat(capturedRequest.getFilename(), is(fileName));
        assertThat(capturedRequest.getInitialComment(), is(fileComment));
        assertThat(result, is(mockResponse));
    }

    @Test
    @DisplayName("메시지 예약 전송 시 올바른 요청 객체가 ScheduleService로 전달되어야 한다")
    void scheduleMessageShouldDelegateToScheduleService() {
        // Given
        Message mockMessage = Message.builder()
                .text(messageText)
                .build();

        ScheduleMessageResponse mockResponse = ScheduleMessageResponse.builder()
                .ok(true)
                .message(mockMessage)
                .postAt(123456789)
                .build();

        when(scheduleService.scheduleMessage(any(ScheduleMessageRequest.class))).thenReturn(mockResponse);

        // When
        ScheduleMessageResponse result = slackService.scheduleMessage(messageText, postAtMinutes);

        // Then
        verify(scheduleService).scheduleMessage(scheduleMessageRequestCaptor.capture());
        ScheduleMessageRequest capturedRequest = scheduleMessageRequestCaptor.getValue();

        assertThat(capturedRequest.getChannel(), is(channelId));
        assertThat(capturedRequest.getText(), is(messageText));

        // postAt 시간 (현재 시간 + postAtMinutes분 이내)
        int expectedPostAtTimestamp = (int) Instant.now()
                .plus(postAtMinutes, ChronoUnit.MINUTES)
                .getEpochSecond();

        // 타임스탬프는 정확히 같을 수는 없으므로 약간의 오차 허용 (5초 이내)
        assertThat(capturedRequest.getPostAt(), is(both(greaterThanOrEqualTo(expectedPostAtTimestamp - 5))
                .and(lessThanOrEqualTo(expectedPostAtTimestamp + 5))));

        assertThat(result, is(mockResponse));
    }
}