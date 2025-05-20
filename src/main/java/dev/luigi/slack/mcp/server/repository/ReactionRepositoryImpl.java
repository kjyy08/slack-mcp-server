package dev.luigi.slack.mcp.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.request.ReactionRequest;
import dev.luigi.slack.mcp.server.dto.response.ReactionResponse;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import org.springframework.stereotype.Repository;

@Repository
public class ReactionRepositoryImpl extends AbstractSlackRepository implements ReactionRepository {
    public ReactionRepositoryImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
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
