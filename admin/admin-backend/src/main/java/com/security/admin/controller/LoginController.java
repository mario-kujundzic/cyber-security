package com.security.admin.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.admin.dto.UserDTO;
import com.security.admin.dto.UserTokenStateDTO;
import com.security.admin.exception.UserException;
import com.security.admin.security.auth.JwtAuthenticationRequest;
import com.security.admin.service.UserService;

@RestController
@Validated
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

	private UserService userService;

	@Autowired
	public LoginController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<UserTokenStateDTO> login(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response) throws UserException {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		UserTokenStateDTO token = userService.login(username, password);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	@PostMapping("/forgot-password")
    public ResponseEntity<UserDTO> forgotPassword(@Valid @RequestBody String username) {
        userService.forgotPassword(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
