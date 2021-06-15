package com.security.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import com.security.admin.dto.GenericMessageDTO;
import com.security.admin.dto.ModifyUserRequestDTO;
import com.security.admin.service.CertificateService;
import com.security.admin.service.ModifyUserRequestService;

@RestController
@RequestMapping(value = "/api/modifyUserRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class ModifyUserRequestController {

	private ModifyUserRequestService service;

	private CertificateService certService;
	@Autowired
	public ModifyUserRequestController(ModifyUserRequestService service, CertificateService certService) {
		this.service = service;
		this.certService = certService;
	}

	@PostMapping("/request")
	public ResponseEntity<Object> requestNewCertificate(@RequestBody @Valid ModifyUserRequestDTO dto,
			HttpServletRequest request) {
		try {
			certService.checkCertificateFromRequest(request);
			service.addRequest(dto);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(
				new GenericMessageDTO(
						"User role change request successfully created! Wait for the Super Admin to respond."),
				HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getCertificate(@PathVariable long id) {
		ModifyUserRequestDTO dto = service.getRequest(id);

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping()
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getAllRequests() {
		List<ModifyUserRequestDTO> dtos = service.getAllRequestsDTO();

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@GetMapping("/confirm/{id}")
	@PreAuthorize("hasAuthority('UPDATE_PRIVILEGE')")
	public ResponseEntity<Object> confirmRequest(@PathVariable long id) throws IOException {
		GenericMessageDTO response = service.confirmRequest(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/decline/{id}")
	@PreAuthorize("hasAuthority('UPDATE_PRIVILEGE')")
	public ResponseEntity<Object> declineRequest(@PathVariable long id) throws IOException {
		GenericMessageDTO response = service.declineRequest(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
