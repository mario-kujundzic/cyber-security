package com.security.admin.security.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttemptEventDTO {
    private String username;
    private LoginAttemptResult result;
    private String IPAddress;
    private long lastLoginUnixSeconds;
}
