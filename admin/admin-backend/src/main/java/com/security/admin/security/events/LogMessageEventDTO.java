package com.security.admin.security.events;

import com.security.admin.enums.LogMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogMessageEventDTO {
    private long unixSecondsTimestamp;
    private LogMessageType type;
    private String content;

    @Override
    public String toString() {
        return new Date(unixSecondsTimestamp) + " [" + type.name() + "] " + content;
    }
}
