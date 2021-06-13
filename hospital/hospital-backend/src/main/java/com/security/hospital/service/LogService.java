package com.security.hospital.service;

import com.google.gson.Gson;
import com.security.hospital.enums.LogMessageType;
import com.security.hospital.security.events.LogMessageEventDTO;
import com.security.hospital.util.FileUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class LogService {

    @Autowired
    private SecurityEventService securityEventService;

    private final static String logsFolderPath = "logs/";

    private void logMessage(String source, LogMessageType type, String content) {
        long unixSeconds = (new Date()).getTime() / 1000;
        LogMessageEventDTO message = new LogMessageEventDTO(unixSeconds, type, content);
        String logLine = message.toString();

        System.out.println(source + " -> " + logLine);
        String filePath = logsFolderPath + source + ".log";

        try {
            tryCreateFolder();
        } catch (IOException e) {
            System.out.println("LogService ERROR: Can't create log folder" + e.getMessage());
        }

        try {
            FileUtility.appendLine(logsFolderPath + source + ".log", logLine);
        } catch (IOException e) {
            System.out.println("LogService ERROR: Can't write log line '" + logLine + "' to file '" + filePath + "': " + e.getMessage());
            return;
        }

        //securityEventService.invokeLogMessageCreated(message);
    }

    public String loadLogsAsJson(long minUnixSeconds) throws Exception {
        HashMap<String, String[]> logLinesMap = loadLogLines(minUnixSeconds);
        Gson gson = new Gson();

        return gson.toJson(logLinesMap);
    }

    public HashMap<String, String[]> loadLogLines(long minUnixSeconds) throws Exception {
        HashMap<String, String[]> logLinesMap = new HashMap<>();
        tryCreateFolder();

        File directory = new File(logsFolderPath);
        for (File file : directory.listFiles()) {
            if (!file.getName().endsWith(".log")) {
                continue;
            }

            ArrayList<String> lines = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (LogMessageEventDTO.lineIsAfterTime(line, minUnixSeconds)) {
                    lines.add(line);
                }
            }

            String[] tokens = file.getName().split("\\.");
            String source = "";
            for (int i = 0; i < tokens.length - 1; i++) {
                source += tokens[i];
            }

            String[] arr = new String[lines.size()];
            logLinesMap.put(source, lines.toArray(arr));
        }

        return logLinesMap;
    }

    private void tryCreateFolder() throws IOException {
        if (!FileUtility.folderExists(logsFolderPath)) {
            FileUtility.createFolder(logsFolderPath);
        }
    }

    public void logHospitalInfo(String message) {
        logMessage("hospital", LogMessageType.INFO, message);
    }

    public void logHospitalWarning(String message) {
        logMessage("hospital", LogMessageType.WARNING, message);
    }

    public void logHospitalError(String message) {
        logMessage("hospital", LogMessageType.ERROR, message);
    }

    public void logDeviceInfo(String deviceId, String message) {
        logMessage("device_" + deviceId, LogMessageType.INFO, message);
    }

    public void logDeviceWarning(String deviceId, String message) {
        logMessage("device_" + deviceId, LogMessageType.WARNING, message);
    }

    public void logDeviceError(String deviceId, String message) {
        logMessage("device_" + deviceId, LogMessageType.ERROR, message);
    }

}
