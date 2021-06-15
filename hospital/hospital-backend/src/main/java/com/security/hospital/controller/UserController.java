package com.security.hospital.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.dto.UserDTO;
import com.security.hospital.dto.UserListDTO;
import com.security.hospital.dto.UserListRequestDTO;
import com.security.hospital.model.User;
import com.security.hospital.service.AddUserRequestService;
import com.security.hospital.service.DeleteUserRequestService;
import com.security.hospital.service.ModifyUserRequestService;
import com.security.hospital.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;
	private DeleteUserRequestService deleteUserRequestService;
	private AddUserRequestService addUserRequestService;
	private ModifyUserRequestService modifyUserRequestService;

	@Autowired
	public UserController(UserService userService, DeleteUserRequestService deleteUserRequestService,
			AddUserRequestService addUserRequestService, ModifyUserRequestService modifyUserRequestService) {
		this.userService = userService;
		this.deleteUserRequestService = deleteUserRequestService;
		this.addUserRequestService = addUserRequestService;
		this.modifyUserRequestService = modifyUserRequestService;
	}

	@GetMapping()
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	public ResponseEntity<Object> getUsers() {
		List<UserDTO> users = userService.getAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/request")
	public ResponseEntity<Object> requestUsers(@RequestBody UserListRequestDTO dto) throws Exception {
		UserListDTO users = userService.getAllRequest(dto);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping()
	@PreAuthorize("hasAuthority('CREATE_PRIVILEGE')")
	public ResponseEntity<Object> addUser(@RequestBody @Valid UserDTO dto, @AuthenticationPrincipal User admin) throws IOException {
		GenericMessageDTO message = addUserRequestService.create(dto, admin);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('UPDATE_PRIVILEGE')")
	public ResponseEntity<Object> changeUserRole(@PathVariable("id") Long id, @AuthenticationPrincipal User admin) throws IOException {
		GenericMessageDTO message = modifyUserRequestService.create(id, admin);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('DELETE_PRIVILEGE')")
	public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id, @AuthenticationPrincipal User admin)
			throws IOException {
		GenericMessageDTO dto = deleteUserRequestService.create(id, admin);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

}
