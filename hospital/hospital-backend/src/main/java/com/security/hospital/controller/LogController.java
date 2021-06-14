package com.security.hospital.controller;

import com.google.gson.Gson;
import com.security.hospital.dto.LogSourcesDTO;
import com.security.hospital.dto.LogMessageDTO;
import com.security.hospital.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    public ResponseEntity<String> getLogsSince(@RequestParam(value = "since", required = false) Long sinceUnixSeconds, @RequestParam(value = "sources", required = false) String[] sources) throws Exception {
        if (sinceUnixSeconds == null) {
            sinceUnixSeconds = 0L;
        }

        if (sources == null) {
            sources = new String[0];
        }

        HashMap<String, ArrayList<LogMessageDTO>> logMap = logService.loadLogLinesSince(sinceUnixSeconds);

        Gson gson = new Gson();

        if (sources.length == 0) {
            String json = gson.toJson(logMap);
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        HashMap<String, ArrayList<LogMessageDTO>> filteredMap = new HashMap<>();
        for (String source : sources) {
            if (logMap.containsKey(source)) {
                filteredMap.put(source, logMap.get(source));
                continue;
            }

            ArrayList<LogMessageDTO> temp = new ArrayList<>();
            temp.add(new LogMessageDTO((new Date()).getTime(), null,"The source '" + source + "' wasn't found in the hospital's log archive."));
            filteredMap.put(source, temp);
        }

        return new ResponseEntity<>(gson.toJson(filteredMap), HttpStatus.OK);
    }

    @GetMapping("/sources")
    public ResponseEntity<Object> getLogSources() throws IOException {
        LogSourcesDTO dto;
        String[] sourceNames = logService.findLogSources();
        dto = new LogSourcesDTO(sourceNames);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
