package com.security.hospital.service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.hospital.dto.CertificateDTO;
import com.security.hospital.model.Certificate;
import com.security.hospital.repository.CertificateRepository;

@Service
public class CertificateService {
	private CertificateRepository repository;
	
	@Autowired
	public CertificateService(CertificateRepository repository) {
		this.repository = repository;
	}
	
	public List<CertificateDTO> getAll() {
		return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	private CertificateDTO toDTO(Certificate c) {
		return new CertificateDTO(c);
	}

	public CertificateDTO revokeCertificate(String serialNumber, String revocationReason) {
		Certificate cert = repository.findOneBySerialNumber(new BigInteger(serialNumber));
		cert.setRevocationReason(revocationReason);
		cert.setRevocationStatus(true);
		// send drugom bekendu
		// ako je sve okej, onda tek sejv, inace neki exception baci jer nesto nije okej
		repository.save(cert);
		return new CertificateDTO(cert);
	}
}
