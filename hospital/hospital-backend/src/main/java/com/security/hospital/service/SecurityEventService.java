package com.security.hospital.service;

import com.security.hospital.enums.LogMessageType;
import com.security.hospital.security.events.DOSEventDTO;
import com.security.hospital.security.events.LogMessageEventDTO;
import com.security.hospital.security.events.LoginAttemptEventDTO;
import com.security.hospital.security.events.LoginAttemptResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class SecurityEventService {
    private final int BATCH_SIZE = 100; // Max requests to send at once
    private final int BUFFER_SIZE_BATCHES = 10; // Max number of batches to keep in memory - if overflown,
                                                // the oldest one is discarded
    private final float SEND_INTERVAL_SECONDS = 1; // How often to send a batch of requests

    private final String loginEventUrl = "https://localhost:9001/api/securityEvents/loginAttempt";
    private final String logMessageEventUrl = "https://localhost:9001/api/securityEvents/logMessage";
    private final String dosEventUrl = "https://localhost:9001/api/securityEvents/dos";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    public void invokeLoginAttempt(String username, LoginAttemptResult result, String IPAddress,
                                   long lastLoginUnixSeconds) {
        LoginAttemptEventDTO dto = new LoginAttemptEventDTO(username, result, IPAddress, lastLoginUnixSeconds);

        try {
            restTemplate.postForObject(loginEventUrl, dto, String.class);
        } catch (RestClientException e) {
            System.out.println("[ERROR, Security Events] Can't POST login attempt: " + e.getMessage());
        }
    }

    public void invokeLogMessageCreated(LogMessageEventDTO dto) {
        try {
            restTemplate.postForObject(logMessageEventUrl, dto, String.class);
        } catch (RestClientException e) {
            System.out.println("[ERROR, Security Events] Can't POST log message created: " + e.getMessage());
        }
    }

    public void invokeDoSSuspected(int numberOfRequests, float intervalSeconds, String IPAddress) {
        DOSEventDTO dto = new DOSEventDTO(numberOfRequests, intervalSeconds, IPAddress);

        try {
            restTemplate.postForObject(dosEventUrl, dto, String.class);
        } catch (RestClientException e) {
            System.out.println("[ERROR, Security Events] Can't POST DoS suspected: " + e.getMessage());
        }
    }
}
