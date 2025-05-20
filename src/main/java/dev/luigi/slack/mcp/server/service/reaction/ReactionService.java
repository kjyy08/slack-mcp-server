package dev.luigi.slack.mcp.server.service.reaction;

import dev.luigi.slack.mcp.server.dto.request.ReactionRequest;
import dev.luigi.slack.mcp.server.dto.response.ReactionResponse;

public interface ReactionService {
    ReactionResponse addReaction(ReactionRequest req);

    ReactionResponse removeReaction(ReactionRequest req);

}
