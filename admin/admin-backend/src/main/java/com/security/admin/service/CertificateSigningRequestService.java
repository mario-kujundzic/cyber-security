package com.security.admin.service;

import com.security.admin.dto.CertificateSigningRequestDTO;
import com.security.admin.model.CertificateSigningRequest;
import com.security.admin.model.CertificateSigningRequestStatus;
import com.security.admin.repository.CertificateSigningRequestRepository;

import lombok.NonNull;

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

		save(request);
	}
	
	public void save(CertificateSigningRequest req) {
		repository.save(req);
	}

	public CertificateSigningRequestDTO getUnsignedRequestDTO(long id) {
		return toDTO(repository.getOneByIdAndStatus(id, CertificateSigningRequestStatus.PENDING));
	}

	private CertificateSigningRequestDTO toDTO(CertificateSigningRequest req) {
		CertificateSigningRequestDTO dto = new CertificateSigningRequestDTO(req.getCommonName(), req.getOrganization(),
				req.getOrganizationUnit(), req.getLocality(), req.getState(), req.getCountry(), req.getCommonName(),
				req.getPublicKey());
		return dto;
	}

	public CertificateSigningRequest getOne(Long requestId) {
		return repository.getOne(requestId);
	}
}