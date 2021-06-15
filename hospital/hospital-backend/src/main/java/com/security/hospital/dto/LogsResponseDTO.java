package com.security.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogsResponseDTO {
    private HashMap<String, ArrayList<LogMessageDTO>> logs;
    private ArrayList<ActivatedLogAlarmDTO> activatedAlarms;
}
