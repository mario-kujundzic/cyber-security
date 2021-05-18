package com.security.admin.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
		
	@Column(name = "serial_number")
	private BigInteger serialNumber;
	
	@Column(name = "valid_from")
	private Date validFrom;
	
	@Column(name = "valid_to")
	private Date validTo;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "commonName")
	private String commonName;
	
	@Column(name = "revocation_status")
	private boolean revocationStatus;
	
	@Column(name = "revocation_reason")
	private String revocationReason;
	
	@Column(name = "root_authority")
	private boolean rootAuthority;
	
}
