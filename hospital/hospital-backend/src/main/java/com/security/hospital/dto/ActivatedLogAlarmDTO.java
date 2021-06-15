package com.security.hospital.dto;

import com.security.hospital.model.LogAlarm;
import com.security.hospital.util.ValidationUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ActivatedLogAlarmDTO {

    private Long id;
    private String name;

    public ActivatedLogAlarmDTO(LogAlarmDTO alarm) {
        id = alarm.getId();
        name = alarm.getName();
    }
}
