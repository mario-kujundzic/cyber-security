package com.security.hospital.dto;

import java.math.BigInteger;
import java.util.Date;

import com.security.hospital.model.Certificate;
import com.security.hospital.model.requests.CertificateUser;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CertificateDTO {

	private Long id;
		
	private BigInteger serialNumber;
	
	private Date validFrom;
	
	private Date validTo;
	
	private String email;
	
	private String commonName;
	
	private boolean revocationStatus;
	
	private String revocationReason;
	
	private CertificateUser certificateUser;
	
	public CertificateDTO (Certificate c) {
		id = c.getId();
		serialNumber = c.getSerialNumber();
		validFrom = c.getValidFrom();
		validTo = c.getValidTo();
		email = c.getEmail();
		commonName = c.getCommonName();
		revocationStatus = c.isRevocationStatus();
		revocationReason = c.getRevocationReason();
		certificateUser = c.getCertificateUser();
	}
}
