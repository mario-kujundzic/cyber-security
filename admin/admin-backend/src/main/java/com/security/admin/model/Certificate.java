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
@Table(name = "certificates")
@Data
@NoArgsConstructor
public class Certificate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private byte[] encodedPublicKey;
	
	@Column
	private String serialNumber;

	@Column
	private String commonName;

	@Column
	private String locality;

	@Column
	private String state;

	@Column
	private String organizationName;

	@Column
	private String organizationUnitName;

	@Column
	private String email;

	@Column(columnDefinition = "BOOLEAN DEFAULT false")
	private boolean signed;

	@Column(columnDefinition = "BOOLEAN DEFAULT false")
	private boolean revocationStatus;
	

}
