package dev.luigi.slack.mcp.server.service;

import dev.luigi.slack.mcp.server.dto.request.ReactionRequest;
import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.*;

public interface SlackService {
    PostMessageResponse postMessage(String text);

    ChannelHistoryResponse channelHistory(int limit, String cursor);

    UploadFileResponse uploadFile(String filePath, String title, String text);

    UploadFileResponse uploadFileByBase64(String fileData, String title, String text);

    ReactionResponse addReaction(ReactionRequest req);

    ReactionResponse removeReaction(ReactionRequest req);

    ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req);
}
