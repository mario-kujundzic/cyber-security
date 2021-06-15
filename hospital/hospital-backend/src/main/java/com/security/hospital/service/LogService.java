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
        long unixMilis= (new Date()).getTime();
        LogMessageDTO message = new LogMessageDTO(unixMilis, type, content);
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

            int trimTo = file.getName().lastIndexOf(".log");
            String sourceName = file.getName().substring(0, trimTo);
            sources.add(sourceName);
        }

        return sources.toArray(new String[0]);
    }

    public HashMap<String, ArrayList<LogMessageDTO>> loadLogLinesSince(long minUnixMilis) throws Exception {
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
                if (logMessage.getUnixMilis() > minUnixMilis) {
                    messages.add(logMessage);
                }
            }

            int trimTo = file.getName().lastIndexOf(".log");
            String sourceName = file.getName().substring(0, trimTo);

            logLinesMap.put(sourceName, messages);
        }

        return logLinesMap;
    }

    private void tryCreateFolder() throws IOException {
        if (!FileUtility.folderExists(logsFolderPath)) {
            FileUtility.createFolder(logsFolderPath);
        }
    }

    public void logGeneralInfo(String message) {
        logMessage("general", LogMessageType.INFO, message);
    }

    public void logGeneralWarning(String message) {
        logMessage("general", LogMessageType.WARNING, message);
    }

    public void logGeneralError(String message) {
        logMessage("general", LogMessageType.ERROR, message);
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
