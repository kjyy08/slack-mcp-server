package dev.luigi.slack.mcp.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.ScheduleMessageResponse;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleRepositoryImpl extends AbstractSlackRepository implements ScheduleRepository {
    public ScheduleRepositoryImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req) {
        return null;
    }
}
