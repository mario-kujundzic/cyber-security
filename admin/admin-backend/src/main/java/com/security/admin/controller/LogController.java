package com.security.admin.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.security.admin.dto.AdminAuthDTO;
import com.security.admin.dto.HospitalDTO;
import com.security.admin.dto.LogSourcesDTO;
import com.security.admin.pki.util.CryptographicUtility;
import com.security.admin.pki.util.KeyPairUtility;
import com.security.admin.service.HospitalService;
import com.security.admin.service.LogService;
import com.security.admin.dto.LogMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.*;

@RestController
@RequestMapping(value = "/api/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private RestTemplate restTemplate;

    private String resourceFolderPath;
    
    @Autowired
    public LogController(@Value("${server.ssl.key-store-folder}") String resourceFolderPath) {
    	this.resourceFolderPath = resourceFolderPath;
    }
    
    @GetMapping
    public ResponseEntity<String> getLogsSince(@RequestParam(value = "since", required = false) Long sinceUnixSeconds, @RequestParam(value = "sources", required = false) String[] sources) throws Exception {
        if (sinceUnixSeconds == null) {
            sinceUnixSeconds = 0L;
        }

        if (sources == null) {
            sources = new String[0];
        }

        // Local logs
        HashMap<String, ArrayList<LogMessageDTO>> logMap = logService.loadLocalLogLinesSince(sinceUnixSeconds);

        // Logs for each hospital in the system
        ArrayList<HospitalDTO> hospitals = hospitalService.getAll();
        HashMap<String, ArrayList<LogMessageDTO>> hospitalLogMap;
        for (HospitalDTO hospital : hospitals) {
            hospitalLogMap = getHospitalLogs(hospital.getCommonName(), hospital.getHospitalUrl(), sources);
            for (String key : hospitalLogMap.keySet()) {
                logMap.put(hospital.getCommonName() + "." + key, hospitalLogMap.get(key));
            }
        }

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

    private HashMap<String, ArrayList<LogMessageDTO>> getHospitalLogs(String hospitalName, String hospitalUrl, String[] sources) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        AdminAuthDTO dto = getAuthDTO();

        String queryParams = "?sources=";
        if (sources.length > 0) {
            for (String source : sources) {
                if (source.startsWith(hospitalName)) {
                    String sourceName = source.split("\\.")[1];
                    queryParams += sourceName + ",";
                }
            }
        }

        if (queryParams.length() == "?sources=".length()) {
            queryParams = "";
        } else {
            queryParams = queryParams.substring(0, queryParams.length()-1);
        }

        HttpEntity<AdminAuthDTO> entity = new HttpEntity<>(dto, headers);
        String response = this.restTemplate.exchange(hospitalUrl + "/api/logs" + queryParams, HttpMethod.POST, entity, String.class).getBody();
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, ArrayList<LogMessageDTO>>>(){}.getType();
        return gson.fromJson(response, type);
    }

    @GetMapping("/sources")
    public ResponseEntity<Object> getLogSources() throws IOException {
        ArrayList<String> allSources = new ArrayList<>();

        // Local sources
        String[] localSources = logService.findLocalLogSources();
        for (String source : localSources) {
            allSources.add(source);
        }

        // Hospital sources
        ArrayList<HospitalDTO> hospitals = hospitalService.getAll();
        LogSourcesDTO sourcesDTO;
        for (HospitalDTO hospital : hospitals) {
            try {
                sourcesDTO = getHospitalSources(hospital.getHospitalUrl());
            } catch (Exception e) {
                System.out.println("GET Hospital Sources for " + hospital.getHospitalUrl() + " FAILED: " + e.getMessage());
                continue;
            }

            for (String source : sourcesDTO.getSources()) {
                allSources.add(hospital.getCommonName() + "." + source);
            }
        }

        LogSourcesDTO dto = new LogSourcesDTO(allSources.toArray(new String[0]));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private LogSourcesDTO getHospitalSources(String hospitalUrl) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        AdminAuthDTO dto = getAuthDTO();
        HttpEntity<AdminAuthDTO> entity = new HttpEntity<>(dto, headers);

        return this.restTemplate.exchange(hospitalUrl + "/api/logs/sources", HttpMethod.POST, entity, LogSourcesDTO.class).getBody();
    }
    
    private AdminAuthDTO getAuthDTO() throws IOException {
    	AdminAuthDTO dto = new AdminAuthDTO();
		byte[] csrBytes = dto.getCSRBytes();
		PrivateKey privateKey = KeyPairUtility.readPrivateKey(resourceFolderPath + "/key.priv");
		byte[] signature = CryptographicUtility.sign(csrBytes, privateKey);
		String base64Signature = Base64.getEncoder().encodeToString(signature);
		dto.setSignature(base64Signature);
		return dto;
    }
}
