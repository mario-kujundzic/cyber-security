package com.security.hospital.security.events;

import com.security.hospital.enums.LogMessageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Data
public class LogMessageEventDTO {
    private long unixSecondsTimestamp;
    private LogMessageType type;
    private String content;

    private SimpleDateFormat sdf;

    public LogMessageEventDTO() {
        sdf = new SimpleDateFormat("dd.MM.yy kk:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
    }
    public LogMessageEventDTO(long unixSecondsTimestamp, LogMessageType type, String content) {
        this();
        this.unixSecondsTimestamp = unixSecondsTimestamp;
        this.type = type;
        this.content = content;

    }

    @Override
    public String toString() {
        return sdf.format(new Date(unixSecondsTimestamp * 1000)) + " [" + type.name() + "]: " + content;
    }

    public static boolean lineIsAfterTime(String line, long unixSecondsTimestamp) throws Exception {
        String[] tokens = line.split("\\[");
        if (tokens.length != 2) {
            throw new Exception("Can't create LogMessageEventDTO from string: " + line);
        }

        LogMessageEventDTO dto = new LogMessageEventDTO();
        long readSeconds = dto.getSdf().parse(tokens[0]).getTime() / 1000;

        return readSeconds > unixSecondsTimestamp;
    }

    public static LogMessageEventDTO fromString(String line) throws Exception {
        String[] tokens = line.split("\\[");
        if (tokens.length != 2) {
            throw new Exception("Can't create LogMessageEventDTO from string: " + line);
        }

        LogMessageEventDTO dto = new LogMessageEventDTO();
        dto.setUnixSecondsTimestamp(dto.getSdf().parse(tokens[0]).getTime() / 1000);

        tokens = tokens[1].split("] ");
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
