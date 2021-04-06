package com.security.admin.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.admin.model.CertificateSigningRequest;
import com.security.admin.model.CertificateSigningRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CertificateSigningRequestDTO {
	private Long id;

	@NonNull
	@NotBlank(message = "Common Name is required!")
	private String commonName;
	
	@NonNull
	@NotBlank(message = "Organization is required!")
	private String organization;
	
	@NonNull
	@NotBlank(message = "Organizaton Unit is required!")
	private String organizationUnit;
	
	@NonNull
	@NotBlank(message = "Locality is required!")
	private String locality;
	
	@NonNull
	@NotBlank(message = "State is required!")
	private String state;
	
	@NonNull
	@Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be two-letter word")
	private String country;
	
	@NonNull
	@Email(message = "Email should be in valid format!")
	private String email;
	
	@NonNull
	@NotBlank(message = "Public key is required!")
	private String publicKey;

	private CertificateSigningRequestStatus status;

	public CertificateSigningRequestDTO(CertificateSigningRequest csr) {
		commonName = csr.getCommonName();
		organization = csr.getOrganization();
		organizationUnit = csr.getOrganizationUnit();
		locality = csr.getLocality();
		state = csr.getState();
		country = csr.getCountry();
		email = csr.getEmail();
		publicKey = csr.getPublicKey();
		status = csr.getStatus();
		id = csr.getId();
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationUnit() {
		return organizationUnit;
	}

	public void setOrganizationUnit(String organizationUnit) {
		this.organizationUnit = organizationUnit;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public CertificateSigningRequestStatus getStatus() {
		return status;
	}

	public void setStatus(CertificateSigningRequestStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
