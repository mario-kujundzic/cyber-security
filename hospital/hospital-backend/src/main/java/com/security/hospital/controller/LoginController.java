package com.security.hospital.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.hospital.dto.ResetPasswordDTO;
import com.security.hospital.dto.UserTokenStateDTO;
import com.security.hospital.exceptions.MaliciousIPAddressException;
import com.security.hospital.exceptions.OftenUsedPasswordException;
import com.security.hospital.exceptions.UserException;
import com.security.hospital.security.auth.JwtAuthenticationRequest;
import com.security.hospital.service.UserService;

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
    public ResponseEntity<UserTokenStateDTO> login(
            @RequestBody JwtAuthenticationRequest authenticationRequest,
            HttpServletRequest request, HttpServletResponse response) throws DisabledException, UserException, MaliciousIPAddressException {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        UserTokenStateDTO token = userService.login(username, password, request.getRemoteAddr());
        return new ResponseEntity<>(token, HttpStatus.OK);

    }
    
	@PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@Valid @RequestBody String username) {
        userService.forgotPassword(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) throws UserException, OftenUsedPasswordException {
    	userService.resetPassword(dto);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
