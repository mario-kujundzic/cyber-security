package com.security.admin.util;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtility {

    public static void appendLine(String filePath, String line) throws IOException {
        FileWriter fw = new FileWriter(filePath, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line + "\n");
        bw.close();
    }

    public static boolean folderExists(String folderPath) {
        return Files.isDirectory(Paths.get(folderPath));
    }

    public static void createFolder(String folderPath) throws IOException {
        Files.createDirectory(Paths.get(folderPath));
    }
}
