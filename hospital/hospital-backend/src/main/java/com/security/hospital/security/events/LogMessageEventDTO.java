package com.security.hospital.security.events;

import com.security.hospital.enums.LogMessageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
