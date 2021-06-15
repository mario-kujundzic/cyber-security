package com.security.hospital.controller;

import com.security.hospital.dto.LogAlarmDTO;
import com.security.hospital.model.User;
import com.security.hospital.service.LogAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/api/logAlarms", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogAlarmController {

    @Autowired
    private LogAlarmService logAlarmService;

    @GetMapping
    public ResponseEntity<ArrayList<LogAlarmDTO>> getAll(@AuthenticationPrincipal User user) {
        ArrayList<LogAlarmDTO> list = logAlarmService.getAllForUser(user.getId());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LogAlarmDTO> save(@RequestBody@ Valid LogAlarmDTO dto, @AuthenticationPrincipal User user) {
        dto.setUserId(user.getId());
        return new ResponseEntity<>(logAlarmService.createUpdate(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        this.logAlarmService.delete(id);
        return new ResponseEntity<>("Alarm with id " + id + " deleted.", HttpStatus.OK);
    }
}
