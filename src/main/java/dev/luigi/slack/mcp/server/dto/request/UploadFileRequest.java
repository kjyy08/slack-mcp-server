package dev.luigi.slack.mcp.server.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UploadFileRequest {
    private List<String> channels;
    private byte[] fileContent;
    private String filename;
    private String initialComment;
}
