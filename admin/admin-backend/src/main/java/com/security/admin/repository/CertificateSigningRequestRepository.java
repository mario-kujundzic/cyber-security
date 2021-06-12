package com.security.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.admin.model.requests.CertificateSigningRequest;
import com.security.admin.model.requests.RequestStatus;

public interface CertificateSigningRequestRepository extends JpaRepository<CertificateSigningRequest, Long> {

	List<CertificateSigningRequest> findAll();
	List<CertificateSigningRequest> getAllByStatus(RequestStatus status);
	CertificateSigningRequest getOneByCommonName(String commonName);
	CertificateSigningRequest getOneByIdAndStatus(long id, RequestStatus status);
}
