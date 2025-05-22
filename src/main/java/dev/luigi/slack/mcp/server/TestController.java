package dev.luigi.slack.mcp.server;

import dev.luigi.slack.mcp.server.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final SlackService slackService;

    @GetMapping("/message")
    public ResponseEntity<?> postMessage(@RequestParam String text) {
        return ResponseEntity.ok(slackService.postMessage(text));
    }

    @GetMapping("/history")
    public ResponseEntity<?> fetchChannelHistory(@RequestParam int limit, @RequestParam String cursor) {
        return ResponseEntity.ok(slackService.channelHistory(limit, cursor));
    }

    @PostMapping("/files")
    public ResponseEntity<?> uploadFile(@RequestPart String filePath, @RequestPart String title, @RequestPart String text) {
        return ResponseEntity.ok(slackService.uploadFile(filePath, title, text));
    }

    @PostMapping("/files/base64")
    public ResponseEntity<?> uploadFileByBase64(@RequestPart String fileData, @RequestPart String title, @RequestPart String text) {
        return ResponseEntity.ok(slackService.uploadFileByBase64(fileData, title, text));
    }
}
