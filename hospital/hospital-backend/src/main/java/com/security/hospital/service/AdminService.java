package com.security.hospital.service;

import com.security.hospital.dto.UserTokenStateDTO;
import com.security.hospital.exceptions.UserException;
import com.security.hospital.model.User;
import com.security.hospital.repository.UserRepository;
import com.security.hospital.security.CustomUserDetailsService;
import com.security.hospital.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AdminService {

	private UserRepository userRepository;
	private TokenUtils tokenUtils;
	private AuthenticationManager authenticationManager;
	private CustomUserDetailsService userDetailsService;
	private AuthorityService authorityService;

	@Autowired
	public AdminService(UserRepository userRepository, TokenUtils tokenUtils,
                        AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
                        AuthorityService authorityService) {
		this.userRepository = userRepository;
		this.tokenUtils = tokenUtils;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.authorityService = authorityService;
	}



}
