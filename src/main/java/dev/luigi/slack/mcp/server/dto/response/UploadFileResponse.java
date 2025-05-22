package dev.luigi.slack.mcp.server.dto.response;

import dev.luigi.slack.mcp.server.dto.common.FileInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UploadFileResponse extends SlackResponse {
    private FileInfo file;
}
