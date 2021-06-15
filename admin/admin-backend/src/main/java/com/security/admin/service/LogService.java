package com.security.admin.service;

import com.security.admin.dto.HospitalDTO;
import com.security.admin.dto.LogMessageDTO;
import com.security.admin.dto.LogSourcesDTO;
import com.security.admin.dto.UserTokenStateDTO;
import com.security.admin.enums.LogMessageType;
import com.security.admin.security.auth.JwtAuthenticationRequest;
import com.security.admin.util.FileUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

@Service
public class LogService {

    private final static String logsFolderPath = "logs/";

    private void logMessage(String source, LogMessageType type, String content) {
        long unixMilis = (new Date()).getTime();
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

    public String[] findLocalLogSources() throws IOException {
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

    public HashMap<String, ArrayList<LogMessageDTO>> loadLocalLogLinesSince(long minUnixMilis) throws Exception {
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
        logMessage("admin.general", LogMessageType.INFO, message);
    }

    public void logGeneralWarning(String message) {
        logMessage("admin.general", LogMessageType.WARNING, message);
    }

    public void logGeneralError(String message) {
        logMessage("admin.general", LogMessageType.ERROR, message);
    }

    public void logAuthInfo(String message) {
        logMessage("admin.auth", LogMessageType.INFO, message);
    }

    public void logAuthWarning(String message) {
        logMessage("admin.auth", LogMessageType.WARNING, message);
    }

    public void logAuthError(String message) {
        logMessage("admin.auth", LogMessageType.ERROR, message);
    }
}
