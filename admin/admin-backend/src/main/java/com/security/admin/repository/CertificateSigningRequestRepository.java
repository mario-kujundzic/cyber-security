package com.security.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.admin.model.CertificateSigningRequest;
import com.security.admin.model.CertificateSigningRequestStatus;

public interface CertificateSigningRequestRepository extends JpaRepository<CertificateSigningRequest, Long> {
	
	List<CertificateSigningRequest> getAllByStatus(CertificateSigningRequestStatus status);
	CertificateSigningRequest getOneByCommonName(String commonName);
	CertificateSigningRequest getOneByIdAndStatus(long id, CertificateSigningRequestStatus status);
}
