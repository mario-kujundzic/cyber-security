package com.security.admin.controller;

import java.io.IOException;
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

import com.security.admin.dto.AddUserRequestDTO;
import com.security.admin.dto.GenericMessageDTO;
import com.security.admin.service.AddUserRequestService;

@RestController
@RequestMapping(value = "/api/addUserRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddUserRequestController {
	private AddUserRequestService service;

	@Autowired
	public AddUserRequestController (AddUserRequestService service) {
		this.service = service;
	}

	@PostMapping("/request")
	public ResponseEntity<GenericMessageDTO> requestUserAdd(@RequestBody @Valid AddUserRequestDTO dto) {
		try {
			service.addRequest(dto);
		} catch (Exception e) {
			return new ResponseEntity<>(new GenericMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(
				new GenericMessageDTO(
						"User addition request successfully created! Wait for the Super Admin to respond."),
				HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getRequest(@PathVariable long id) {
		AddUserRequestDTO dto = service.getRequest(id);

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping()
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getAllRequests() {
		List<AddUserRequestDTO> dtos = service.getAllRequestsDTO();

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
