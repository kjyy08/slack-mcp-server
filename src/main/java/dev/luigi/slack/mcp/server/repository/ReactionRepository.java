package dev.luigi.slack.mcp.server.repository;

import dev.luigi.slack.mcp.server.dto.request.ReactionRequest;
import dev.luigi.slack.mcp.server.dto.response.ReactionResponse;

public interface ReactionRepository {
    ReactionResponse addReaction(ReactionRequest req);
    ReactionResponse removeReaction(ReactionRequest req);

}
