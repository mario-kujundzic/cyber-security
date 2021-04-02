package com.security.admin.service;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.admin.dto.CertificateDTO;
import com.security.admin.pki.certificate.CertificateGenerator;
import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;
import com.security.admin.pki.keystore.KeyStoreManager;
import com.security.admin.pki.util.KeyIssuerSubjectGenerator;

@Service
public class CertificateService {

	@Autowired
	private KeyStoreManager keyStoreManager;
	
	public List<CertificateDTO> getAll() {
		List<CertificateDTO> list = new ArrayList<>();
		keyStoreManager.readAllCertificates().forEach(c -> list.add(toDTO(c)));
		return list;
	}

	private CertificateDTO toDTO(Certificate c) {
		return new CertificateDTO(c.getPublicKey().getEncoded().toString());
	}

	public CertificateDTO createCertificate(CertificateDTO dto) {
		try {
			KeyPair kp = KeyIssuerSubjectGenerator.generateKeyPair();
			
			SubjectData subjectData = KeyIssuerSubjectGenerator.generateSubjectData("Natasa", "Ivanovic",
					"Baby Center Delivery Clinic", "natasaivanovic@gmail.com", "2021-01-01", "2023-01-01");
			
			IssuerData issuerData = KeyIssuerSubjectGenerator.generateIssuerData(kp.getPrivate(), "Mario", "Kujundzic");
			
			Certificate cert = CertificateGenerator.generateCertificate(subjectData, issuerData);
			
			keyStoreManager.write(dto.getTitle(), kp.getPrivate(), cert);
			keyStoreManager.saveKeyStore();
			return toDTO(cert);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
