package dev.luigi.slack.mcp.server.service.file;

import com.slack.api.RequestConfigurator;
import com.slack.api.bolt.App;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.files.FilesUploadV2Response;
import dev.luigi.slack.mcp.server.dto.request.UploadFileRequest;
import dev.luigi.slack.mcp.server.dto.response.UploadFileResponse;
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

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @Mock
    private App mockApp;

    @Mock
    private MethodsClient mockClient;

    @InjectMocks
    private SlackFileService slackFileService;

    @Mock
    File mockFile;

    private String channel;
    private String title;
    private String filename;
    private String text;

    @BeforeEach
    void setUp() {
        channel = "C1234567890";
        title = "Test File";
        filename = "test.txt";
        text = "This is a test file content.";
    }

    @Test
    @DisplayName("슬랙 파일 업로드 전송이 정상적으로 수행되어야 한다.")
    void uploadFileSucceeds() throws SlackApiException, IOException {
        // Given
        UploadFileRequest request = UploadFileRequest.builder()
                .channel(channel)
                .file(mockFile)
                .title(title)
                .filename(filename)
                .initialComment(text)
                .build();

        com.slack.api.model.File mockSlackFile = com.slack.api.model.File.builder()
                .title(title)
                .size(1024) // Mock size
                .build();

        FilesUploadV2Response resp = new FilesUploadV2Response();
        resp.setOk(true);
        resp.setError(null);
        resp.setFile(mockSlackFile);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.filesUploadV2(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        UploadFileResponse result = slackFileService.uploadFile(request);

        // Then
        assertThat(result.isOk(), is(true));
        assertThat(result.getFile().getTitle(), is(title));
        assertThat(result.getError(), is(nullValue()));
        assertThat(result.getFile().getSize(), is(1024));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).filesUploadV2(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("슬랙 파일 업로드 전송에 실패하면 적절한 에러 응답을 반환해야 한다.")
    void uploadFileFails() throws SlackApiException, IOException {
        // Given
        UploadFileRequest request = UploadFileRequest.builder()
                .channel(channel)
                .file(mockFile)
                .title(title)
                .filename(filename)
                .initialComment(text)
                .build();

        com.slack.api.model.File mockSlackFile = com.slack.api.model.File.builder()
                .title("")
                .size(0)
                .build();

        FilesUploadV2Response resp = new FilesUploadV2Response();
        resp.setOk(false);
        resp.setError("invalid_file");
        resp.setFile(mockSlackFile);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.filesUploadV2(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        UploadFileResponse result = slackFileService.uploadFile(request);

        // Then
        assertThat(result.isOk(), is(false));
        assertThat(result.getError(), is("invalid_file"));
        assertThat(result.getFile().getTitle(), is(""));
        assertThat(result.getFile().getSize(), is(0));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).filesUploadV2(any(RequestConfigurator.class));
    }


    @Test
    @DisplayName("슬랙 파일 업로드 중 Slack API 예외 발생 시 적절히 처리되어야 한다.")
    void uploadFileThrowsSlackApiException() throws SlackApiException, IOException {
        // Given
        UploadFileRequest request = UploadFileRequest.builder()
                .channel(channel)
                .file(mockFile)
                .title(title)
                .filename(filename)
                .initialComment(text)
                .build();

        Response response = new Response.Builder()
                .request(new Request.Builder()
                        .url("https://slack.com/api/files.upload.v2")
                        .build())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("Bad Request")
                .build();

        String errorResponseBody = "{\"ok\":false,\"error\":\"invalid_auth\"}";

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.filesUploadV2(any(RequestConfigurator.class))).thenThrow(new SlackApiException(response, errorResponseBody));

        // When & Then
        assertThatThrownBy(() -> slackFileService.uploadFile(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to upload file");
    }

    @Test
    @DisplayName("슬랙 파일 업로드 중 IO 예외 발생 시 적절히 처리되어야 한다.")
    void uploadFileThrowsIOException() throws SlackApiException, IOException {
        // Given
        UploadFileRequest request = UploadFileRequest.builder()
                .channel(channel)
                .file(mockFile)
                .title(title)
                .filename(filename)
                .initialComment(text)
                .build();

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.filesUploadV2(any(RequestConfigurator.class))).thenThrow(new IOException("Network error"));

        // When & Then
        assertThatThrownBy(() -> slackFileService.uploadFile(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to upload file");
    }

    @Test
    @DisplayName("슬랙 파일 Base64 업로드가 정상적으로 수행되어야 한다.")
    void uploadFileByBase64Succeeds() throws SlackApiException, IOException {
        // Given
        byte[] fileData = "base64EncodedData".getBytes();
        UploadFileRequest request = UploadFileRequest.builder()
                .channel(channel)
                .fileData(fileData)
                .title(title)
                .filename(filename)
                .initialComment(text)
                .build();

        com.slack.api.model.File mockSlackFile = com.slack.api.model.File.builder()
                .title(title)
                .size(2048)
                .build();

        FilesUploadV2Response resp = new FilesUploadV2Response();
        resp.setOk(true);
        resp.setError(null);
        resp.setFile(mockSlackFile);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.filesUploadV2(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        UploadFileResponse result = slackFileService.uploadFileByBase64(request);

        // Then
        assertThat(result.isOk(), is(true));
        assertThat(result.getFile().getTitle(), is(title));
        assertThat(result.getError(), is(nullValue()));
        assertThat(result.getFile().getSize(), is(2048));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).filesUploadV2(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("슬랙 파일 Base64 업로드 전송에 실패하면 적절한 에러 응답을 반환해야 한다.")
    void uploadFileByBase64Fails() throws SlackApiException, IOException {
        // Given
        byte[] fileData = "base64EncodedData".getBytes();
        UploadFileRequest request = UploadFileRequest.builder()
                .channel(channel)
                .fileData(fileData)
                .title(title)
                .filename(filename)
                .initialComment(text)
                .build();

        com.slack.api.model.File mockSlackFile = com.slack.api.model.File.builder()
                .title("")
                .size(0)
                .build();

        FilesUploadV2Response resp = new FilesUploadV2Response();
        resp.setOk(false);
        resp.setError("invalid_file_type");
        resp.setFile(mockSlackFile);

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.filesUploadV2(any(RequestConfigurator.class))).thenReturn(resp);

        // When
        UploadFileResponse result = slackFileService.uploadFileByBase64(request);

        // Then
        assertThat(result.isOk(), is(false));
        assertThat(result.getError(), is("invalid_file_type"));
        assertThat(result.getFile().getTitle(), is(""));
        assertThat(result.getFile().getSize(), is(0));

        InOrder inOrder = inOrder(mockApp, mockClient);
        inOrder.verify(mockApp).client();
        inOrder.verify(mockClient).filesUploadV2(any(RequestConfigurator.class));
    }

    @Test
    @DisplayName("슬랙 파일 Base64 업로드 중 Slack API 예외 발생 시 적절히 처리되어야 한다.")
    void uploadFileByBase64ThrowsSlackApiException() throws SlackApiException, IOException {
        // Given
        UploadFileRequest request = UploadFileRequest.builder()
                .channel(channel)
                .fileData("base64EncodedData".getBytes())
                .title(title)
                .filename(filename)
                .initialComment(text)
                .build();

        Response response = new Response.Builder()
                .request(new Request.Builder()
                        .url("https://slack.com/api/files.upload.v2")
                        .build())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("Bad Request")
                .build();

        String errorResponseBody = "{\"ok\":false,\"error\":\"invalid_auth\"}";

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.filesUploadV2(any(RequestConfigurator.class))).thenThrow(new SlackApiException(response, errorResponseBody));

        // When & Then
        assertThatThrownBy(() -> slackFileService.uploadFileByBase64(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to upload file");
    }

    @Test
    @DisplayName("슬랙 파일 Base64 업로드 중 IO 예외 발생 시 적절히 처리되어야 한다.")
    void uploadFileByBase64ThrowsIOException() throws SlackApiException, IOException {
        // Given
        UploadFileRequest request = UploadFileRequest.builder()
                .channel(channel)
                .fileData("base64EncodedData".getBytes())
                .title(title)
                .filename(filename)
                .initialComment(text)
                .build();

        when(mockApp.client()).thenReturn(mockClient);
        when(mockClient.filesUploadV2(any(RequestConfigurator.class))).thenThrow(new IOException("Network error"));

        // When & Then
        assertThatThrownBy(() -> slackFileService.uploadFileByBase64(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to upload file");
    }
}