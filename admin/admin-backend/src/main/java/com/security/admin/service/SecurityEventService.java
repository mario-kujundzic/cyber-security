package com.security.admin.service;

import com.security.admin.security.events.LogMessageEventDTO;
import com.security.admin.security.events.LoginAttemptResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class SecurityEventService {

    public void invokeLoginAttempt(String username, LoginAttemptResult result, String IPAddress,
                                   long lastLoginUnixSeconds) {

    }

    public void invokeLogMessageCreated(LogMessageEventDTO dto) {
        // Nothing so far, might be useful in the future
    }

    public void invokeDoSSuspected(int numberOfRequests, float intervalSeconds, String IPAddress) {

    }
}
