package com.security.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.security.admin.dto.CertificateSigningRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
	
	@Column(name = "public_key", columnDefinition = "TEXT")
	private String publicKey;

	@Column(name = "email")
	private String email;
	
	@Column(name = "status")
	private CertificateSigningRequestStatus status;

	public CertificateSigningRequest(CertificateSigningRequestDTO dto) {
		commonName = dto.getCommonName();
		organization = dto.getOrganization();
		organizationUnit = dto.getOrganizationUnit();
		locality = dto.getLocality();
		state = dto.getState();
		country = dto.getCountry();
		publicKey = dto.getPublicKey();
		email = dto.getEmail();
		status = CertificateSigningRequestStatus.PENDING;
	}

	@Override
	public String toString() {
		return "CertificateSigningRequest{" +
				"id=" + id +
				", commonName='" + commonName + '\'' +
				", organization='" + organization + '\'' +
				", organizationUnit='" + organizationUnit + '\'' +
				", locality='" + locality + '\'' +
				", state='" + state + '\'' +
				", country='" + country + '\'' +
				", publicKey='" + publicKey + '\'' +
				", status=" + status +
				'}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
