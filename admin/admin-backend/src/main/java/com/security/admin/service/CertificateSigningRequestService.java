package com.security.admin.service;

import com.security.admin.dto.CertificateSigningRequestDTO;
import com.security.admin.model.CertificateSigningRequest;
import com.security.admin.repository.CertificateSigningRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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