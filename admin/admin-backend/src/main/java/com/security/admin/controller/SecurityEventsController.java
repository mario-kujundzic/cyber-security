package com.security.admin.controller;

import com.security.admin.security.events.DOSEventDTO;
import com.security.admin.security.events.LogMessageEventDTO;
import com.security.admin.security.events.LoginAttemptEventDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/securityEvents", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityEventsController {

    @PostMapping("/loginAttempt")
    public ResponseEntity<String> loginAttemptReceived(@RequestBody LoginAttemptEventDTO dto) {
        System.out.println("Login attempt: " + dto);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @PostMapping("/logMessage")
    public ResponseEntity<String> logMessageReceived(@RequestBody LogMessageEventDTO dto) {
        System.out.println("Log message: " + dto);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @PostMapping("/dos")
    public ResponseEntity<String> dosSuspected(@RequestBody DOSEventDTO dto) {
        System.out.println("DoS suspected: " + dto);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }
}
