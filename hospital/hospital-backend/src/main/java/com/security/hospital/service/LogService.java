package com.security.hospital.service;

import com.security.hospital.enums.LogMessageType;
import com.security.hospital.dto.LogMessageDTO;
import com.security.hospital.util.FileUtility;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class LogService {

    private final static String logsFolderPath = "logs/";

    private void logMessage(String source, LogMessageType type, String content) {
        long unixSeconds = (new Date()).getTime() / 1000;
        LogMessageDTO message = new LogMessageDTO(unixSeconds, type, content);
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
    }

    public String[] findLogSources() throws IOException {
        ArrayList<String> sources = new ArrayList<>();

        tryCreateFolder();

        File directory = new File(logsFolderPath);
        for (File file : directory.listFiles()) {
            if (!file.getName().endsWith(".log")) {
                continue;
            }

            sources.add(file.getName().split("\\.")[0]);
        }

        return sources.toArray(new String[0]);
    }

    public HashMap<String, ArrayList<LogMessageDTO>> loadLogLinesSince(long minUnixSeconds) throws Exception {
        HashMap<String, ArrayList<LogMessageDTO>> logLinesMap = new HashMap<>();
        tryCreateFolder();

        File directory = new File(logsFolderPath);
        for (File file : directory.listFiles()) {
            if (!file.getName().endsWith(".log")) {
                continue;
            }

            ArrayList<LogMessageDTO> messages = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                LogMessageDTO logMessage = LogMessageDTO.fromString(line);
                if (logMessage.getUnixSeconds() >= minUnixSeconds) {
                    messages.add(logMessage);
                }
            }

            String[] tokens = file.getName().split("\\.");
            String source = "";
            for (int i = 0; i < tokens.length - 1; i++) {
                source += tokens[i];
            }

            logLinesMap.put(source, messages);
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

    public void logAuthInfo(String message) {
        logMessage("auth", LogMessageType.INFO, message);
    }

    public void logAuthWarning(String message) {
        logMessage("auth", LogMessageType.WARNING, message);
    }

    public void logAuthError(String message) {
        logMessage("auth", LogMessageType.ERROR, message);
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
