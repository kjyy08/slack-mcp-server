package dev.luigi.slack.mcp.server.service.file;

import com.slack.api.bolt.App;
import com.slack.api.methods.response.files.FilesUploadV2Response;
import com.slack.api.model.File;
import dev.luigi.slack.mcp.server.dto.common.FileInfo;
import dev.luigi.slack.mcp.server.dto.request.UploadFileRequest;
import dev.luigi.slack.mcp.server.dto.response.UploadFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackFileService implements FileService {
    private final App app;

    @Override
    public UploadFileResponse uploadFile(UploadFileRequest req) {
        try {
            FilesUploadV2Response filesUploadV2Response = app.client().filesUploadV2(r -> r
                    .channel(req.getChannel())
                    .file(req.getFile())
                    .filename(req.getFilename())
                    .title(req.getTitle())
                    .initialComment(req.getInitialComment())
            );

            return convertToUploadFileResponse(filesUploadV2Response);
        } catch (Exception e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public UploadFileResponse uploadFileByBase64(UploadFileRequest req) {
        try {
            FilesUploadV2Response filesUploadV2Response = app.client().filesUploadV2(r -> r
                    .channel(req.getChannel())
                    .fileData(req.getFileData())
                    .filename(req.getFilename())
                    .title(req.getTitle())
                    .initialComment(req.getInitialComment())
            );

            return convertToUploadFileResponse(filesUploadV2Response);
        } catch (Exception e) {
            log.error("Failed to upload file by base64: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file by base64", e);
        }
    }

    private UploadFileResponse convertToUploadFileResponse(FilesUploadV2Response filesUploadV2Response) {
        FileInfo file = convertToFileInfo(filesUploadV2Response.getFile());

        return UploadFileResponse.builder()
                .ok(filesUploadV2Response.isOk())
                .error(filesUploadV2Response.getError())
                .file(file)
                .build();
    }

    private FileInfo convertToFileInfo(File file) {
        return FileInfo.builder()
                .title(file.getTitle())
                .size(file.getSize())
                .build();
    }
}
