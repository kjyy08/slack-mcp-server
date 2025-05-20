package dev.luigi.slack.mcp.server.repository;

import dev.luigi.slack.mcp.server.dto.request.PostMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.PostMessageResponse;

public interface MessageRepository {
    PostMessageResponse postMessage(PostMessageRequest req);

}
