package dev.luigi.slack.mcp.server.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Message {
    private String user;
    private String text;
    private String ts;
    private List<Block> blocks;
}
