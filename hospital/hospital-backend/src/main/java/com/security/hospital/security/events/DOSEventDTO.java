package com.security.hospital.security.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DOSEventDTO {
    private int numberOfRequests;
    private float intervalSeconds;
    private String IPAddress;
}
