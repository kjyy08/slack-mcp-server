package dev.luigi.slack.mcp.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public abstract class AbstractSlackRepository {
    protected final CustomHttpClient httpClient;
    protected final ObjectMapper objectMapper;

    @Value("${slack.slack-bot-token}")
    protected String token;
}
