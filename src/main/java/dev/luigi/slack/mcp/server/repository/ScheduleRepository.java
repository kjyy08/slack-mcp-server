package dev.luigi.slack.mcp.server.repository;

import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.ScheduleMessageResponse;

public interface ScheduleRepository {
    ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req);

}
