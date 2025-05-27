package dev.luigi.slack.mcp.server.service;

import dev.luigi.slack.mcp.server.dto.response.ChannelHistoryResponse;
import dev.luigi.slack.mcp.server.dto.response.PostMessageResponse;
import dev.luigi.slack.mcp.server.dto.response.ScheduleMessageResponse;
import dev.luigi.slack.mcp.server.dto.response.UploadFileResponse;

public interface SlackService {
    PostMessageResponse postMessage(String text);

    ChannelHistoryResponse channelHistory(int limit, String cursor);

    UploadFileResponse uploadFile(String filePath, String title, String filename, String text);

    UploadFileResponse uploadFileByBase64(String fileData, String title, String filename, String text);

    ScheduleMessageResponse scheduleMessage(String text, int postAt);
}
