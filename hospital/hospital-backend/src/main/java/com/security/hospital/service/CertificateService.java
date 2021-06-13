package com.security.hospital.service;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.security.hospital.dto.CertificateDTO;
import com.security.hospital.dto.CertificateStatusDTO;
import com.security.hospital.enums.CertificateStatus;
import com.security.hospital.model.Certificate;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;
import com.security.hospital.repository.CertificateRepository;

@Service
public class CertificateService {
	private CertificateRepository repository;
	private String resourceFolderPath;
	private RestTemplate restTemplate;

	@Autowired
	public CertificateService(@Value("${server.ssl.key-store-folder}") String resourceFolderPath,
			CertificateRepository repository, RestTemplate restTemplate) {
		this.repository = repository;
		this.resourceFolderPath = resourceFolderPath;
		this.restTemplate = restTemplate;
	}

	public List<CertificateDTO> getAll() {
		return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	private CertificateDTO toDTO(Certificate c) {
		return new CertificateDTO(c);
	}

	public CertificateDTO revokeCertificate(String serialNumber, String revocationReason) {
		Certificate cert = repository.findOneBySerialNumber(new BigInteger(serialNumber));
		cert.setRevocationReason(revocationReason);
		cert.setRevocationStatus(true);
		// send drugom bekendu
		// ako je sve okej, onda tek sejv, inace neki exception baci jer nesto nije okej
		repository.save(cert);
		return new CertificateDTO(cert);
	}

	public CertificateDTO checkStatus(String serialNumber) throws Exception {
		String adminEndpointURI = "https://localhost:9001/api/certificates/status/" + serialNumber;

		CertificateStatusDTO status = this.restTemplate.getForObject(adminEndpointURI,
				CertificateStatusDTO.class);

		PublicKey rootCAKey = KeyPairUtility.readPublicKey(resourceFolderPath + "/rootCA.pub");

		if (rootCAKey == null) {
			throw new Exception(
					"Denied: Admin app public key not registered. Contact a super admin to obtain the public key.");
		}

		// Verify signature
		byte[] csrBytes = status.getCSRBytes();
		byte[] signature = Base64.getDecoder().decode(status.getSignature());
		boolean valid = CryptographicUtility.verify(csrBytes, signature, rootCAKey);

		if (!valid) {
			throw new Exception("Denied: signature invalid.");
		}
		
		if (status.getStatus() == CertificateStatus.NOT_EXIST) {
			throw new Exception("Certificate with serial number: " + serialNumber + " doesn't exist.");
		}
		
		if (status.getStatus() == CertificateStatus.EXPIRED) {
			revokeCertificate(serialNumber, "Certificate expired");
		}
		
		Certificate cert = repository.findOneBySerialNumber(new BigInteger(serialNumber));
		CertificateDTO dto = new CertificateDTO(cert);

		return dto; 
	}
	
}
