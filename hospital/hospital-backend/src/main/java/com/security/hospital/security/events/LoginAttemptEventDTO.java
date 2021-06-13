package com.security.hospital.security.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttemptEventDTO {
    private String username;
    private LoginAttemptResult result;
    private String IPAddress;
    private long lastLoginUnixSeconds;
}
