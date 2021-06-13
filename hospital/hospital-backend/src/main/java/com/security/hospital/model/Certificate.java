package com.security.hospital.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.security.hospital.model.requests.CertificateUser;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
public class Certificate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(name = "serial_number", unique = true)
	private BigInteger serialNumber;
	
	@Column(name = "valid_from")
	private Date validFrom;
	
	@Column(name = "valid_to")
	private Date validTo;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "common_name")
	private String commonName;
	
	@Column(name = "revocation_status")
	private boolean revocationStatus;
	
	@Column(name = "revocation_reason", nullable = true)
	private String revocationReason;
	
	@Column(name = "certificate_user")
	private CertificateUser certificateUser;
}
