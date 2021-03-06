package com.security.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.admin.enums.LogMessageType;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
public class LogMessageDTO {
    private long unixMilis;
    private LogMessageType type;
    private String content;

    @JsonIgnore
    private transient SimpleDateFormat sdf;

    public LogMessageDTO() {
        sdf = new SimpleDateFormat("dd.MM.yy kk:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        unixMilis = 0;
        type = null;
        content = null;
    }

    public LogMessageDTO(long unixMilis, LogMessageType type, String content) {
        this();
        this.unixMilis = unixMilis;
        this.type = type;
        this.content = content;

    }

    @Override
    public String toString() {
        return sdf.format(new Date(unixMilis)) + " [" + type.name() + "]: " + content;
    }

    public static boolean lineIsAfterTime(String line, long unixSecondsTimestamp) throws Exception {
        String[] tokens = line.split("\\[");
        if (tokens.length != 2) {
            throw new Exception("Can't create LogMessageEventDTO from string: " + line);
        }

        LogMessageDTO dto = new LogMessageDTO();
        long readSeconds = dto.getSdf().parse(tokens[0]).getTime() / 1000;

        return readSeconds > unixSecondsTimestamp;
    }

    public static LogMessageDTO fromString(String line) throws Exception {
        String[] tokens = line.split("\\[");
        if (tokens.length != 2) {
            throw new Exception("Can't create LogMessageEventDTO from string: " + line);
        }

        LogMessageDTO dto = new LogMessageDTO();
        dto.setUnixMilis(dto.getSdf().parse(tokens[0]).getTime());

        tokens = tokens[1].split("]: ");
        switch (tokens[0]) {
            case "INFO":
                dto.setType(LogMessageType.INFO);
                break;
            case "WARNING":
                dto.setType(LogMessageType.WARNING);
                break;
            case "ERROR":
                dto.setType(LogMessageType.ERROR);
                break;
            default:
                throw new Exception("Can't create LogMessageEventDTO from string, invalid TYPE: " + line);
        }

        dto.setContent(tokens[1]);

        return dto;
    }
}
