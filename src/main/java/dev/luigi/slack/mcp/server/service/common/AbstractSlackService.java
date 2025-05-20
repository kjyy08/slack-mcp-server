package dev.luigi.slack.mcp.server.service.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public abstract class AbstractSlackService {
    protected final CustomHttpClient httpClient;
    protected final ObjectMapper objectMapper;

    @Value("${slack.slack-bot-token}")
    protected String token;
}
