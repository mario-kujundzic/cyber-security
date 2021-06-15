package com.security.hospital.controller;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.security.hospital.dto.AdminAuthDTO;
import com.security.hospital.dto.LogMessageDTO;
import com.security.hospital.dto.LogSourcesDTO;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;
import com.security.hospital.service.LogService;

@RestController
@RequestMapping(value = "/api/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogController {

	@Autowired
	private LogService logService;

	private String resourceFolderPath;

	@Autowired
	public LogController(@Value("${server.ssl.key-store-folder}") String resourceFolderPath) {
		this.resourceFolderPath = resourceFolderPath;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<String> getLogsSince(@RequestParam(value = "since", required = false) Long sinceUnixSeconds,
			@RequestParam(value = "sources", required = false) String[] sources) throws Exception {
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
			temp.add(new LogMessageDTO((new Date()).getTime(), null,
					"The source '" + source + "' wasn't found in the hospital's log archive.", null));
			filteredMap.put(source, temp);
		}

		return new ResponseEntity<>(gson.toJson(filteredMap), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<String> requestLogsAdmin(
			@RequestBody @Valid AdminAuthDTO adminAuth,
			@RequestParam(value = "since", required = false) Long sinceUnixSeconds,
			@RequestParam(value = "sources", required = false) String[] sources) throws Exception {
		authorizeAdminApp(adminAuth);
		return getLogsSince(sinceUnixSeconds, sources);
	}
	
	@GetMapping("/sources")
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getLogSources() throws IOException {
		LogSourcesDTO dto;
		String[] sourceNames = logService.findLogSources();
		dto = new LogSourcesDTO(sourceNames);

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PostMapping("/sources")
	public ResponseEntity<Object> requestLogSources(@RequestBody @Valid AdminAuthDTO adminAuth) throws Exception {
		authorizeAdminApp(adminAuth);
		return getLogSources();
	}
	
	private void authorizeAdminApp(AdminAuthDTO adminAuth) throws Exception {
		PublicKey rootCAKey = KeyPairUtility.readPublicKey(resourceFolderPath + "/rootCA.pub");

		if (rootCAKey == null) {
			throw new Exception("Denied: no admin public key registered.");
		}

		// Verify signature
		byte[] csrBytes = adminAuth.getCSRBytes();
		byte[] signature = Base64.getDecoder().decode(adminAuth.getSignature());
		boolean valid = CryptographicUtility.verify(csrBytes, signature, rootCAKey);

		if (!valid) {
			throw new Exception("Denied: signature invalid.");
		}
	}
}
