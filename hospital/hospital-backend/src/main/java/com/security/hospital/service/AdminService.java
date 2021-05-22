package com.security.hospital.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.util.Base64;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.security.hospital.dto.CertificateRequestDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.model.Admin;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;

@Service
public class AdminService {
	private String resourceFolderPath;

	private RestTemplate restTemplate;

	@Autowired
	public AdminService(@Value("${server.ssl.key-store-folder}") String resourceFolderPath, RestTemplate restTemplate) {
		this.resourceFolderPath = resourceFolderPath;
		this.restTemplate = restTemplate;
	}

	public GenericMessageDTO makeCertificateRequest(CertificateRequestDTO certificateRequest, String adminEndpointURI,
			Admin admin) throws Exception {
		// Add email and public key
//		RestTemplate restTemplate = new RestTemplate();
		certificateRequest.setEmail(admin.getUsername());

		String publicKeyPEM = KeyPairUtility.readPEM(resourceFolderPath + "/key.pub");
		certificateRequest.setPublicKey(publicKeyPEM);

		// Add signature
		byte[] csrBytes = certificateRequest.getCSRBytes();
		PrivateKey privateKey = KeyPairUtility.readPrivateKey(resourceFolderPath + "/key.priv");
		byte[] signature = CryptographicUtility.sign(csrBytes, privateKey);
		String base64Signature = Base64.getEncoder().encodeToString(signature);
		certificateRequest.setSignature(base64Signature);
		
		GenericMessageDTO csrResponse = this.restTemplate.postForObject(adminEndpointURI, certificateRequest, GenericMessageDTO.class);
		
		return csrResponse;
	}

}
