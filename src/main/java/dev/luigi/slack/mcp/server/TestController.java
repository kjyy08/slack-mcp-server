package dev.luigi.slack.mcp.server;

import dev.luigi.slack.mcp.server.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final SlackService slackService;

//    @PostMapping
//    public ResponseEntity<?> postMessage(@RequestBody String text) {
//        return ResponseEntity.ok(slackService.postMessage(text));
//    }
}
