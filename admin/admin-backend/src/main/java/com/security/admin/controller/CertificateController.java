package com.security.admin.controller;

import java.util.List;

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

import com.security.admin.dto.CertificateDTO;
import com.security.admin.exception.UserException;
import com.security.admin.service.CertificateService;

@RestController
@RequestMapping(value = "/api/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

	private CertificateService certService;

	@Autowired
	public CertificateController(CertificateService certService) {
		this.certService = certService;
	}

	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CertificateDTO>> getAllCerts() throws UserException {
		List<CertificateDTO> results = certService.getAll();
		return new ResponseEntity<>(results, HttpStatus.OK);
	}

	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CertificateDTO> createCert(@RequestBody CertificateDTO dto) throws UserException {
		CertificateDTO created = certService.createCertificate(dto);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}
	
	@PostMapping("/{serialNumber}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CertificateDTO> revokeCert(@PathVariable("serialNumber") String serialNumber, @RequestBody String revocationReason) {
		CertificateDTO revoked = null;
		try {
			revoked = certService.revokeCertificate(serialNumber, revocationReason);
			// posalji potpisano da li je revoked ili ne
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(revoked, HttpStatus.OK);
	}
	
	@GetMapping("/revoked")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CertificateDTO>> getAllRevokedCerts() {
		List<CertificateDTO> revoked = certService.getRevokedCerts();
		return new ResponseEntity<>(revoked, HttpStatus.OK);
	}
	
}
