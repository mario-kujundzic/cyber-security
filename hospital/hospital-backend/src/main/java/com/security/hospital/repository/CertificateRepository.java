package com.security.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

}
