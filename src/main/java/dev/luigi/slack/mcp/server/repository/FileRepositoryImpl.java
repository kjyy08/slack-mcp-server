package dev.luigi.slack.mcp.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.request.UploadFileRequest;
import dev.luigi.slack.mcp.server.dto.response.UploadFileResponse;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepositoryImpl extends AbstractSlackRepository implements FileRepository {
    public FileRepositoryImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public UploadFileResponse uploadFile(UploadFileRequest req) {
        return null;
    }
}
