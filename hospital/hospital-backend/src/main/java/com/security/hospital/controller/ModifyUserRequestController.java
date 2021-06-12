package com.security.hospital.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.dto.ModifyUserRequestDTO;
import com.security.hospital.service.ModifyUserRequestService;


@RestController
@RequestMapping(value = "/api/modifyUserRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class ModifyUserRequestController {

	private ModifyUserRequestService service;

	@Autowired
	public ModifyUserRequestController (ModifyUserRequestService service) {
		this.service = service;
	}

	@PostMapping("/response")
	public ResponseEntity<GenericMessageDTO> confirmUserModify(@RequestBody @Valid ModifyUserRequestDTO dto) {
		try {
			service.processRequestResponse(dto);
		} catch (Exception e) {
			return new ResponseEntity<>(new GenericMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(
				new GenericMessageDTO(
						"User role change request successfully confirmed!"),
				HttpStatus.OK);
	}

	@GetMapping()
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getAllRequests() {
		List<ModifyUserRequestDTO> dtos = service.getAllRequestsDTO();

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

}
