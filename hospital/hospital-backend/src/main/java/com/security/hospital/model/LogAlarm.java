package com.security.hospital.model;

import com.security.hospital.dto.LogAlarmDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "log_alarms")
public class LogAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "name", unique = false, nullable = false)
    private String name;

    @NonNull
    @Column(name = "when_source_is", unique = false, nullable = false)
    private String whenSourceIs;

    @NonNull
    @Column(name = "when_content_has", unique = false, nullable = false)
    private String whenContentHas;

    @NonNull
    @Column(name = "user_id", unique = false, nullable = false)
    private Long userId;

    public LogAlarm(LogAlarmDTO dto) {
        id = dto.getId();
        name = dto.getName();
        whenSourceIs = dto.getWhenSourceIs();
        whenContentHas = dto.getWhenContentHas();
        userId = dto.getUserId();
    }
}
