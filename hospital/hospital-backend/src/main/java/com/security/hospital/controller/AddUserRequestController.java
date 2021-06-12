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

import com.security.hospital.dto.AddUserRequestDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.service.AddUserRequestService;


@RestController
@RequestMapping(value = "/api/addUserRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddUserRequestController {
	private AddUserRequestService service;

	@Autowired
	public AddUserRequestController (AddUserRequestService service) {
		this.service = service;
	}

	@PostMapping("/response")
	public ResponseEntity<GenericMessageDTO> confirmUserAdd(@RequestBody @Valid AddUserRequestDTO dto) {
		try {
			service.processRequestResponse(dto);
		} catch (Exception e) {
			return new ResponseEntity<>(new GenericMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(
				new GenericMessageDTO(
						"User addition request successfully confirmed!"),
				HttpStatus.OK);
	}

	@GetMapping()
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getAllRequests() {
		List<AddUserRequestDTO> dtos = service.getAllRequestsDTO();

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
}
