package com.security.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class CertificateSigningRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "common_name")
	private String commonName;
	
	@Column(name = "organization")
	private String organization;
	
	@Column(name = "organization_unit")
	private String organizationUnit;
	
	@Column(name = "locality")
	private String locality;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "public_key")
	private byte[] publicKey;
	
	@Column(name = "status")
	private CertificateSigningRequestStatus status;
	

}
