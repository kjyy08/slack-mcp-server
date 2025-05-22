package dev.luigi.slack.mcp.server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.File;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadFileRequest {
    private String channel;
    private File file;
    private byte[] fileData;
    private String title;
    private String filename;
    private String initialComment;
}
