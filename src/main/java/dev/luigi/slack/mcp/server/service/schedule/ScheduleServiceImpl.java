package dev.luigi.slack.mcp.server.service.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.ScheduleMessageResponse;
import dev.luigi.slack.mcp.server.service.common.AbstractSlackService;
import dev.luigi.slack.mcp.server.util.CustomHttpClient;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl extends AbstractSlackService implements ScheduleService {
    public ScheduleServiceImpl(CustomHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req) {
        return null;
    }
}
