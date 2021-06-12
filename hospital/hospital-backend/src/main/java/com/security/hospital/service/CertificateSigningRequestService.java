package com.security.hospital.service;

import java.security.PrivateKey;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.security.hospital.dto.CertificateRequestDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.model.User;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;

@Service
public class CertificateSigningRequestService {
	private String resourceFolderPath;

	private RestTemplate restTemplate;

	@Autowired
	public CertificateSigningRequestService(@Value("${server.ssl.key-store-folder}") String resourceFolderPath, RestTemplate restTemplate) {
		this.resourceFolderPath = resourceFolderPath;
		this.restTemplate = restTemplate;
	}

	public GenericMessageDTO makeCertificateRequest(CertificateRequestDTO certificateRequest, String adminEndpointURI,
			User admin) throws Exception {
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
