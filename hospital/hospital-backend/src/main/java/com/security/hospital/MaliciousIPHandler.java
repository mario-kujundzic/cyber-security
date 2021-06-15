package com.security.hospital;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaliciousIPHandler {
	
	public static List<String> ips = new ArrayList<>();
	
	public static void loadIP() {
		try (Stream<String> lines = Files.lines(Paths.get("./src/main/resources/malicious-ip.txt"))) {
			ips = lines.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}       
	}
	
	public static void writeIP(String IP) {
		Path out = Paths.get("./src/main/resources/malicious-ip.txt");
		try {
			Files.write(out, ips, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
	