package com.security.admin.repository;

import com.security.admin.model.LogAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface LogAlarmRepository extends JpaRepository<LogAlarm, Long> {
    public ArrayList<LogAlarm> findAllByUserId(Long userId);
}
