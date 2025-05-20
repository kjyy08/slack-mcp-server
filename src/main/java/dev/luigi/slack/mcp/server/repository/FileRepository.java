package dev.luigi.slack.mcp.server.repository;

import dev.luigi.slack.mcp.server.dto.request.UploadFileRequest;
import dev.luigi.slack.mcp.server.dto.response.UploadFileResponse;

public interface FileRepository {
    UploadFileResponse uploadFile(UploadFileRequest req);

}
