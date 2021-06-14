package com.security.admin.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.admin.model.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
	
	Certificate findOneBySerialNumber(BigInteger serialNumber);
	Certificate findOneBySerialNumber(String serialNumber);
	List<Certificate> findAllByRevocationStatus(boolean revocationStatus);

}
