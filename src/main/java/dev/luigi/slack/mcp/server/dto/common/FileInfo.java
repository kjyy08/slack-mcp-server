package dev.luigi.slack.mcp.server.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfo {
    private String id;
    private String name;
    private Long size;
}
