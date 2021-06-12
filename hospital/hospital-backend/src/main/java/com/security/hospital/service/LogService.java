package com.security.hospital.service;

import com.security.hospital.enums.LogMessageType;
import com.security.hospital.security.events.LogMessageEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogService {

    @Autowired
    private SecurityEventService securityEventService;

    private void logMessage(LogMessageType type, String content) {
        long unixSeconds = (new Date()).getTime() / 1000;
        LogMessageEventDTO message = new LogMessageEventDTO(unixSeconds, type, content);
        System.out.println(message);

        securityEventService.invokeLogMessageCreated(message);
    }

    public void logInfo(String message) {
        logMessage(LogMessageType.INFO, message);
    }

    public void logWarning(String message) {
        logMessage(LogMessageType.WARNING, message);
    }

    public void logError(String message) {
        logMessage(LogMessageType.ERROR, message);
    }
}
