package com.security.hospital.service;

import com.security.hospital.certificates.CertificateRequest;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.dto.RefinedCertificateSigningRequestDTO;
import com.security.hospital.dto.UserTokenStateDTO;
import com.security.hospital.exceptions.UserException;
import com.security.hospital.model.Admin;
import com.security.hospital.model.User;
import com.security.hospital.repository.UserRepository;
import com.security.hospital.security.CustomUserDetailsService;
import com.security.hospital.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Service
public class AdminService {

	private UserRepository userRepository;
	private TokenUtils tokenUtils;
	private AuthenticationManager authenticationManager;
	private CustomUserDetailsService userDetailsService;
	private AuthorityService authorityService;

	private String resourceFolderPath;

	@Autowired
	public AdminService(UserRepository userRepository, TokenUtils tokenUtils,
                        AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
                        AuthorityService authorityService, @Value("${server.ssl.key-store-folder}") String resourceFolderPath) {
		this.userRepository = userRepository;
		this.tokenUtils = tokenUtils;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.authorityService = authorityService;
		this.resourceFolderPath = resourceFolderPath;
	}

	public GenericMessageDTO makeCertificateRequest(CertificateRequest certificateRequest, String adminEndpointURI, Admin admin) throws IOException, RestClientException {
		RestTemplate restTemplate = new RestTemplate();

		// Add email and public key
		RefinedCertificateSigningRequestDTO refinedDTO = new RefinedCertificateSigningRequestDTO(certificateRequest);
		refinedDTO.setEmail(admin.getUsername());

		byte[] bytes = Files.readAllBytes(Paths.get(resourceFolderPath + "/key.pub"));
		refinedDTO.setPublicKey(new String(bytes));

		GenericMessageDTO csrResponse;

		csrResponse = restTemplate.postForObject(adminEndpointURI, refinedDTO, GenericMessageDTO.class);

		return csrResponse;
	}



}
