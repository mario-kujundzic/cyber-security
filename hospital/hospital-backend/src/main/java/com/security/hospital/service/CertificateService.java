package com.security.hospital.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.security.hospital.dto.AddCertificateRequestDTO;
import com.security.hospital.dto.CertificateDTO;
import com.security.hospital.dto.RevokeCertRequestDTO;
import com.security.hospital.model.Certificate;
import com.security.hospital.model.requests.RequestStatus;
import com.security.hospital.dto.CertificateStatusDTO;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;
import com.security.hospital.repository.CertificateRepository;

@Service
public class CertificateService {
	private CertificateRepository repository;

	private RestTemplate restTemplate;

	private String resourceFolderPath;

	@Autowired
	public CertificateService(@Value("${server.ssl.key-store-folder}") String resourceFolderPath,
			CertificateRepository repository, RestTemplate restTemplate) {
		this.repository = repository;
		this.restTemplate = restTemplate;
		this.resourceFolderPath = resourceFolderPath;
	}

	public List<CertificateDTO> getAll() {
		return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	private CertificateDTO toDTO(Certificate c) {
		return new CertificateDTO(c);
	}

	public CertificateDTO requestCertificateRevokation(String serialNumber, String revocationReason) throws Exception {
		Certificate check = repository.findOneBySerialNumber(new BigInteger(serialNumber));
		if (check.isRevocationStatus())
			throw new Exception("Certificate already revoked!");
		RevokeCertRequestDTO dto = new RevokeCertRequestDTO();
		dto.setSerialNumber(serialNumber);
		dto.setRevocationReason(revocationReason);
		dto.setStatus(RequestStatus.PENDING);
		dto.setHospitalName("Hospital1");

		byte[] csrBytes = dto.getCSRBytes();
		PrivateKey privateKey = KeyPairUtility.readPrivateKey(resourceFolderPath + "/key.priv");
		byte[] signature = CryptographicUtility.sign(csrBytes, privateKey);
		String base64Signature = Base64.getEncoder().encodeToString(signature);
		dto.setSignature(base64Signature);

		String adminEndpointURI = "https://localhost:9001/api/certificates/requestRevoke";

		RevokeCertRequestDTO response = this.restTemplate.postForObject(adminEndpointURI, dto,
				RevokeCertRequestDTO.class);

		PublicKey rootCAKey = KeyPairUtility.readPublicKey(resourceFolderPath + "/rootCA.pub");

		if (rootCAKey == null) {
			throw new Exception(
					"Denied: Admin app public key not registered. Contact a super admin to obtain the public key.");
		}

		csrBytes = response.getCSRBytes();
		signature = Base64.getDecoder().decode(response.getSignature());
		boolean valid = CryptographicUtility.verify(csrBytes, signature, rootCAKey);

		if (!valid) {
			throw new Exception("Denied: signature invalid.");
		}

		Certificate cert = revokeCertificate(serialNumber, revocationReason);
		return new CertificateDTO(cert);
	}

	public CertificateDTO checkStatus(String serialNumber) throws Exception {
		String adminEndpointURI = "https://localhost:9001/api/certificates/status/" + serialNumber;

		CertificateStatusDTO status = this.restTemplate.getForObject(adminEndpointURI, CertificateStatusDTO.class);

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
		Certificate cert = null;
		switch (status.getStatus()) {
		case EXPIRED:
			cert = revokeCertificate(serialNumber, "Certificate expired");
			break;
		case REVOKED:
			cert = revokeCertificate(serialNumber, status.getRevocationReason());
			break;
		case ACTIVE:
			cert = repository.findOneBySerialNumber(new BigInteger(serialNumber));
			break;
		case NOT_EXIST:
			throw new Exception("Certificate with serial number: " + serialNumber + " doesn't exist.");
		}
		CertificateDTO dto = new CertificateDTO(cert);

		return dto;
	}

	private Certificate revokeCertificate(String serialNumber, String revocationReason) {
		Certificate cert = repository.findOneBySerialNumber(new BigInteger(serialNumber));
		cert.setRevocationReason(revocationReason);
		cert.setRevocationStatus(true);
		cert.setValidTo(new Date());
		return repository.save(cert);
	}

	public void saveCertificateToDb(AddCertificateRequestDTO dto) throws Exception {
		PublicKey rootCAKey = KeyPairUtility.readPublicKey(resourceFolderPath + "/rootCA.pub");

		if (rootCAKey == null) {
			throw new Exception(
					"Denied: Admin app public key not registered. Contact a super admin to obtain the public key.");
		}
		
		// Verify signature
		byte[] csrBytes = dto.getCSRBytes();
		byte[] signature = Base64.getDecoder().decode(dto.getSignature());
		boolean valid = CryptographicUtility.verify(csrBytes, signature, rootCAKey);

		if (!valid) {
			throw new Exception("Denied: signature invalid.");
		}
		
		com.security.hospital.model.Certificate cert = new Certificate();
		cert.setCommonName(dto.getCommonName());
		cert.setEmail(dto.getEmail());
		cert.setRevocationStatus(false);
		cert.setSerialNumber(dto.getSerialNumber());
		cert.setValidFrom(new Date(dto.getValidFrom()));
		cert.setValidTo(new Date(dto.getValidTo()));
		cert.setCertificateUser(dto.getCertificateUser());
		
		repository.save(cert);
	}

}
