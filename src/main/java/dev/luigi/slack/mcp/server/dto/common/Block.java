package dev.luigi.slack.mcp.server.dto.common;

import lombok.Data;

@Data
public class Block {
    private String type;
    private Section section;
    private Divider divider;
    private Actions actions;
    private Image image;
    private Context context;
    private Header header;
}