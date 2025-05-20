package dev.luigi.slack.mcp.server.service.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.request.UploadFileRequest;
import dev.luigi.slack.mcp.server.dto.response.UploadFileResponse;
import dev.luigi.slack.mcp.server.service.common.AbstractSlackService;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends AbstractSlackService implements FileService {
    public FileServiceImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public UploadFileResponse uploadFile(UploadFileRequest req) {
        return null;
    }
}
