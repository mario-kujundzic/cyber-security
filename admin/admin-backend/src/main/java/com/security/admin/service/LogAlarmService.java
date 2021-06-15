package com.security.admin.service;

import com.security.admin.repository.LogAlarmRepository;
import com.security.admin.dto.LogAlarmDTO;
import com.security.admin.model.LogAlarm;
import com.security.admin.repository.LogAlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LogAlarmService {

    @Autowired
    LogAlarmRepository repository;

    public ArrayList<LogAlarmDTO> getAllForUser(Long userId) {
        ArrayList<LogAlarmDTO> list = new ArrayList<>();

        for (LogAlarm alarm : repository.findAllByUserId(userId)) {
            list.add(new LogAlarmDTO(alarm));
        }

        return list;
    }

    public LogAlarmDTO getOne(Long id) {
        return new LogAlarmDTO(repository.getOne(id));
    }

    public LogAlarmDTO createUpdate(LogAlarmDTO dto) {
        LogAlarm alarm = new LogAlarm(dto);
        alarm = repository.save(alarm);

        return new LogAlarmDTO(alarm);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
