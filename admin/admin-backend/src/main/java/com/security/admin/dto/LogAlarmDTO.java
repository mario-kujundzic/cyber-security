package com.security.admin.dto;

import com.security.admin.model.LogAlarm;
import com.security.admin.util.ValidationUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogAlarmDTO {
    private Long id;

    @NonNull
    @NotBlank(message = "Alarm name is required!")
    @Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Alarm name must be alphanumeric!")
    private String name;

    @NonNull
    @NotBlank(message = "WhenSourceIs is required!")
    @Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "WhenSourceIs must be alphanumeric!")
    private String whenSourceIs;

    @NonNull
    @NotBlank(message = "WhenContentHas is required!")
    @Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "WhenContentHas must be alphanumeric!")
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
