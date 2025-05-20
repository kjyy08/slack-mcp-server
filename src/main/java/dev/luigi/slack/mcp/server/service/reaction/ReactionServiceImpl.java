package dev.luigi.slack.mcp.server.service.reaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.request.ReactionRequest;
import dev.luigi.slack.mcp.server.dto.response.ReactionResponse;
import dev.luigi.slack.mcp.server.service.common.AbstractSlackService;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import org.springframework.stereotype.Service;

@Service
public class ReactionServiceImpl extends AbstractSlackService implements ReactionService {
    public ReactionServiceImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public ReactionResponse addReaction(ReactionRequest req) {
        return null;
    }

    @Override
    public ReactionResponse removeReaction(ReactionRequest req) {
        return null;
    }
}
