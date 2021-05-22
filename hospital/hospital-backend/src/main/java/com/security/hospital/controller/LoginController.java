package com.security.hospital.controller;

import javax.servlet.http.HttpServletResponse;

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

import com.security.hospital.dto.UserTokenStateDTO;
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
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                                       HttpServletResponse response) throws DisabledException, UserException {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        UserTokenStateDTO token = userService.login(username, password);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
