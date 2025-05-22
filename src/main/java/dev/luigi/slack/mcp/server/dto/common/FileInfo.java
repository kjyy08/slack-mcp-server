package dev.luigi.slack.mcp.server.dto.common;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private String title;
    private Integer size;
}
