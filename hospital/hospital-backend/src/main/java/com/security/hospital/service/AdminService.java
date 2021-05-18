package com.security.hospital.service;

import com.security.hospital.dto.CertificateRequestDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.model.Admin;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;
import com.security.hospital.repository.UserRepository;
import com.security.hospital.security.CustomUserDetailsService;
import com.security.hospital.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Base64;

@Service
public class AdminService {

	private UserRepository userRepository;
	private TokenUtils tokenUtils;
	private AuthenticationManager authenticationManager;
	private CustomUserDetailsService userDetailsService;

	private String resourceFolderPath;

	@Autowired
	public AdminService(UserRepository userRepository, TokenUtils tokenUtils,
			AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
			@Value("${server.ssl.key-store-folder}") String resourceFolderPath) {
		this.userRepository = userRepository;
		this.tokenUtils = tokenUtils;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.resourceFolderPath = resourceFolderPath;
	}

	public GenericMessageDTO makeCertificateRequest(CertificateRequestDTO certificateRequest, String adminEndpointURI,
			Admin admin) throws IOException, RestClientException {
		RestTemplate restTemplate = new RestTemplate();

		// Add email and public key
		certificateRequest.setEmail(admin.getUsername());

		String publicKeyPEM = KeyPairUtility.readPEM(resourceFolderPath + "/key.pub");
		certificateRequest.setPublicKey(publicKeyPEM);

		// Add signature
		byte[] csrBytes = certificateRequest.getCSRBytes();
		PrivateKey privateKey = KeyPairUtility.readPrivateKey(resourceFolderPath + "/key.priv");
		byte[] signature = CryptographicUtility.sign(csrBytes, privateKey);
		String base64Signature = Base64.getEncoder().encodeToString(signature);
		certificateRequest.setSignature(base64Signature);

		GenericMessageDTO csrResponse;

		csrResponse = restTemplate.postForObject(adminEndpointURI, certificateRequest, GenericMessageDTO.class);

		return csrResponse;
	}

}
