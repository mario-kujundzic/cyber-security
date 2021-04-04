package com.security.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
public class Certificate {
	
	@NonNull
	@Column(columnDefinition = "BLOB(2048) NOT NULL")
	private byte[] encodedPublicKey;
	
	@NonNull
	@Column
	private String serialNumber;

	@NonNull
	@Column
	private String commonName;

	@NonNull
	@Column
	private String locality;

	@NonNull
	@Column
	private String state;

	@NonNull
	@Column
	private String organizationName;

	@NonNull
	@Column
	private String organizationUnitName;

	@NonNull
	@Column
	private String email;

	@Column(columnDefinition = "BOOLEAN DEFAULT false")
	private boolean signed;

	@Column(columnDefinition = "BOOLEAN DEFAULT false")
	private boolean revocationStatus;

}
