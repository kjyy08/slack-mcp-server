package dev.luigi.slack.mcp.server;

import dev.luigi.slack.mcp.server.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final SlackService slackService;

    @GetMapping
    public ResponseEntity<?> postMessage(@RequestParam String text) {
        return ResponseEntity.ok(slackService.postMessage(text));
    }
}
