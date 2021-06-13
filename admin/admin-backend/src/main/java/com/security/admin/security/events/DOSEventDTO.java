package com.security.admin.security.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DOSEventDTO {
    private int numberOfRequests;
    private float intervalSeconds;
    private String IPAddress;
}
