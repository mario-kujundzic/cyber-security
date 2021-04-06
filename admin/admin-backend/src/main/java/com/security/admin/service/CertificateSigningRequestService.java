package com.security.admin.service;

import com.security.admin.dto.CertificateDTO;
import com.security.admin.dto.CertificateSigningRequestDTO;
import com.security.admin.model.CertificateSigningRequest;
import com.security.admin.pki.certificate.CertificateGenerator;
import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;
import com.security.admin.pki.keystore.KeyStoreManager;
import com.security.admin.pki.util.KeyIssuerSubjectGenerator;
import com.security.admin.repository.CertificateSigningRequestRepository;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateSigningRequestService {
	private CertificateSigningRequestRepository repository;

	@Autowired
	public CertificateSigningRequestService(CertificateSigningRequestRepository repository) {
		this.repository = repository;
	}

	public void addRequest(CertificateSigningRequestDTO dto) {
		CertificateSigningRequest request = new CertificateSigningRequest(dto);

		repository.save(request);
	}
}