package dev.luigi.slack.mcp.server.service.message;

import dev.luigi.slack.mcp.server.dto.request.PostMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.PostMessageResponse;

public interface MessageService {
    PostMessageResponse postMessage(PostMessageRequest req);

}
