package dev.luigi.slack.mcp.server.service.file;

import dev.luigi.slack.mcp.server.dto.request.UploadFileRequest;
import dev.luigi.slack.mcp.server.dto.response.UploadFileResponse;

public interface FileService {
    UploadFileResponse uploadFile(UploadFileRequest req);

}
