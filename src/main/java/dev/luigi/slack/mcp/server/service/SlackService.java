package dev.luigi.slack.mcp.server.service;

import dev.luigi.slack.mcp.server.dto.request.ReactionRequest;
import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.request.UploadFileRequest;
import dev.luigi.slack.mcp.server.dto.response.*;

public interface SlackService {
    PostMessageResponse postMessage(String text);

    FetchHistoryResponse fetchChannelHistory(int limit, String cursor);

    UploadFileResponse uploadFile(UploadFileRequest req);

    ReactionResponse addReaction(ReactionRequest req);

    ReactionResponse removeReaction(ReactionRequest req);

    ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req);
}
