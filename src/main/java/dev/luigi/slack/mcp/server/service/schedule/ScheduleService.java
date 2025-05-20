package dev.luigi.slack.mcp.server.service.schedule;

import dev.luigi.slack.mcp.server.dto.request.ScheduleMessageRequest;
import dev.luigi.slack.mcp.server.dto.response.ScheduleMessageResponse;

public interface ScheduleService {
    ScheduleMessageResponse scheduleMessage(ScheduleMessageRequest req);

}
