package com.security.hospital.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.hospital.dto.AddCertificateRequestDTO;
import com.security.hospital.dto.CertificateDTO;
import com.security.hospital.service.CertificateService;
import com.security.hospital.util.ValidationUtility;

@RestController
@RequestMapping(value = "/api/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {
	private CertificateService service;

	@Autowired
	public CertificateController (CertificateService service) {
		this.service = service;
	}
	

	@GetMapping()
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getAllCertificates() {
		List<CertificateDTO> dtos = service.getAll();

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}


	@PostMapping("/revoke/{serialNumber}")
	@PreAuthorize("hasAuthority('UPDATE_PRIVILEGE')")
	public ResponseEntity<CertificateDTO> revokeCert(@PathVariable("serialNumber") String serialNumber, @RequestBody String revocationReason) {
		if (!ValidationUtility.isEnglishText(revocationReason)) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		CertificateDTO revoked = null;
		try {
			revoked = service.requestCertificateRevokation(serialNumber, revocationReason);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(revoked, HttpStatus.OK);
	}
	
	@GetMapping("/status/{serialNumber}")
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<CertificateDTO> checkStatus(@PathVariable("serialNumber") String serialNumber) throws Exception {
		CertificateDTO dto = service.checkStatus(serialNumber);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Object> addCertificate(@RequestBody @Valid AddCertificateRequestDTO dto) throws Exception {
		service.saveCertificateToDb(dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
