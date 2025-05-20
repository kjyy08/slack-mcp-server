package dev.luigi.slack.mcp.server.dto.response;

import dev.luigi.slack.mcp.server.dto.common.FileInfo;

public class UploadFileResponse extends SlackResponse {
    private FileInfo file;

    public UploadFileResponse(boolean ok, String error, String channel, String ts) {
        super(ok, error, channel, ts);
    }
}
