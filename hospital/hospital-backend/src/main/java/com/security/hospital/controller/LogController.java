package com.security.hospital.controller;

import com.google.gson.Gson;
import com.security.hospital.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("logs")
    public ResponseEntity<String> getLogsSince(@RequestParam(value = "since", required = false) Long sinceUnixSeconds, @RequestParam(value = "sources", required = false) String[] sources) {
        if (sinceUnixSeconds == null) {
            sinceUnixSeconds = 0L;
        }

        if (sources == null) {
            sources = new String[0];
        }

        HashMap<String, String[]> logMap = null;
        try {
            logMap = logService.loadLogLines(sinceUnixSeconds);
        } catch (Exception e) {
            System.out.println("Can't read logs: " + e.getMessage());
        }

        HashMap<String, String[]> filteredMap = new HashMap<>();
        for (String source : sources) {
            if (logMap.containsKey(source)) {
                filteredMap.put(source, logMap.get(source));
                continue;
            }
            filteredMap.put(source, new String[] { "The source '" + source + "' wasn't found in the hospital's log archive." });
        }

        Gson gson = new Gson();
        return new ResponseEntity<String>(gson.toJson(filteredMap), HttpStatus.OK);
    }
}
