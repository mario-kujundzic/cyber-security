package com.security.hospital.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.hospital.dto.CertificateRequestDTO;
import com.security.hospital.dto.DeviceDTO;
import com.security.hospital.dto.DeviceMessageDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.model.User;
import com.security.hospital.service.CertificateService;
import com.security.hospital.service.DeviceService;

@RestController
@RequestMapping(value = "/api/devices", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private CertificateService certService;
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<DeviceDTO> getOneDevices(@PathVariable long id) {
		DeviceDTO deviceDTO = deviceService.getOne(id);

		return new ResponseEntity<DeviceDTO>(deviceDTO, HttpStatus.OK);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<List<DeviceDTO>> getAllDevices() {
		ArrayList<DeviceDTO> deviceDTOs = deviceService.getAll();

		return new ResponseEntity<>(deviceDTOs, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('CREATE_PRIVILEGE')")
	public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO dto) {
		DeviceDTO deviceDTO = deviceService.create(dto);

		return new ResponseEntity<DeviceDTO>(deviceDTO, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('UPDATE_PRIVILEGE')")
	public ResponseEntity<DeviceDTO> updateDevice(@RequestBody DeviceDTO dto) {
		DeviceDTO deviceDTO = deviceService.update(dto);

		return new ResponseEntity<DeviceDTO>(deviceDTO, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('DELETE_PRIVILEGE')")
	public ResponseEntity<GenericMessageDTO> deleteDevice(@PathVariable long id) {
		deviceService.delete(id);

		return new ResponseEntity<>(new GenericMessageDTO("Device with ID " + id + " successfully deleted."),
				HttpStatus.OK);
	}

	@PostMapping("/requestCertificate")
	@PreAuthorize("hasAuthority('CREATE_PRIVILEGE')")
	public ResponseEntity<GenericMessageDTO> requestCertificate(@RequestBody CertificateRequestDTO dto,
			@AuthenticationPrincipal User admin) {
		try {
			GenericMessageDTO mess = deviceService.requestCertificate(dto, admin);
			return new ResponseEntity<>(mess, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new GenericMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestBody DeviceMessageDTO dto, HttpServletRequest request) {
		try {
			deviceService.register(dto);
			return new ResponseEntity<>(new GenericMessageDTO("Successfully registered!"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/message")
	public ResponseEntity<Object> recieveMessage(@RequestBody DeviceMessageDTO dto) {
		try {
			deviceService.processMessage(dto);
			return new ResponseEntity<>(new GenericMessageDTO("Successfully delivered secure message!"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}

