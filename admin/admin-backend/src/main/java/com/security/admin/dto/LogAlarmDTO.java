package com.security.admin.dto;

import com.security.admin.model.LogAlarm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogAlarmDTO {
    private Long id;
    private String name;
    private String whenSourceIs;
    private String whenContentHas;
    private Long userId;

    public LogAlarmDTO(LogAlarm alarm) {
        id = alarm.getId();
        name = alarm.getName();
        whenSourceIs = alarm.getWhenSourceIs();
        whenContentHas = alarm.getWhenContentHas();
        userId = alarm.getUserId();
    }
}
