package com.security.hospital.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.hospital.enums.LogMessageType;

import lombok.Data;

@Data
public class LogMessageDTO {
    private long unixMilis;
    private LogMessageType type;
    private String content;
    private Map<String, Object> params;

    @JsonIgnore
    private transient SimpleDateFormat sdf;

    public LogMessageDTO() {
        sdf = new SimpleDateFormat("dd.MM.yy kk:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        unixMilis = 0;
        type = null;
        content = null;
    	params = new HashMap<>();
    }

    public LogMessageDTO(long unixMilis, LogMessageType type, String content, Map<String, Object> params) {
        this();
        this.unixMilis = unixMilis;
        this.type = type;
        this.content = content;
        if (params == null)
        	this.params = new HashMap<>();
        else
        	this.params = params;
    }

    @Override
    public String toString() {
    	String temp = sdf.format(new Date(unixMilis)) + " [" + type.name() + "]: " + content;
    	if (!params.isEmpty()) {
    		List<String> entries = new ArrayList<String>();
    		for (Entry<String, Object> en : params.entrySet()) {
    			entries.add(en.getKey() + ": " + en.getValue().toString());
    		}    		
    		temp = temp + " - params: " + String.join(", ", entries);
    	}
        return temp;
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
