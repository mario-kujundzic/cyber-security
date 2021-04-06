package com.security.admin.service;

import com.security.admin.dto.CertificateSigningRequestDTO;
import com.security.admin.model.CertificateSigningRequest;
import com.security.admin.model.CertificateSigningRequestStatus;
import com.security.admin.repository.CertificateSigningRequestRepository;

import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		save(request);
	}
	
	public void save(CertificateSigningRequest req) {
		repository.save(req);
	}

	public CertificateSigningRequestDTO getUnsignedRequestDTO(long id) {
		return toDTO(repository.getOneByIdAndStatus(id, CertificateSigningRequestStatus.PENDING));
	}

	public List<CertificateSigningRequestDTO> getAllRequestsDTO() {
		ArrayList<CertificateSigningRequestDTO> list = new ArrayList<>();

		for (CertificateSigningRequest csr : repository.findAll()) {
			list.add(new CertificateSigningRequestDTO(csr));
		}

		return list;
	}

	private CertificateSigningRequestDTO toDTO(CertificateSigningRequest req) {
		CertificateSigningRequestDTO dto = new CertificateSigningRequestDTO(req);
		return dto;
	}

	public CertificateSigningRequest getOne(Long requestId) {
		return repository.getOne(requestId);
	}

	public void declineRequest(long id) {
		CertificateSigningRequest request = repository.getOne(id);
		request.setStatus(CertificateSigningRequestStatus.REJECTED);
		repository.save(request);
	}
}