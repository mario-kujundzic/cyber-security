package com.security.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.admin.model.CertificateUser;
import com.security.admin.model.requests.CertificateSigningRequest;
import com.security.admin.model.requests.RequestStatus;
import com.security.admin.util.ValidationUtility;
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
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Common name must be alphanumeric!")
	private String commonName;
	
	@NonNull
	@NotBlank(message = "Organization is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Organization must be made of alphanumeric characters!")
	private String organization;
	
	@NonNull
	@NotBlank(message = "Organizaton Unit is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Organization unit must be made of alphanumeric characters!")
	private String organizationUnit;
	
	@NonNull
	@NotBlank(message = "Locality is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Locality must be made of alphanumeric characters!")
	private String locality;
	
	@NonNull
	@NotBlank(message = "State is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "State must be made of alphanumeric characters!")
	private String state;
	
	@NonNull
	@Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be two-letter word")
	private String country;
	
	@NonNull
	@Pattern(regexp = ValidationUtility.emailRegex, message = "Email should be in valid format!")
	private String email;
	
	@NonNull
	@NotBlank(message = "Hospital name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Hospital name must be made of alphanumeric characters!")
	private String hospitalName;

	@NonNull
	@NotBlank(message = "Public key is required!")
	@Pattern(regexp = ValidationUtility.PEMRegex, message = "Public key should be in PEM format!")
	private String publicKey;

	@NonNull
	@NotBlank(message = "Signature is required!")
	@Pattern(regexp = ValidationUtility.base64Regex, message = "Signature should be a base64 string!")
	private String signature;
	
	private CertificateUser certificateUser;

	private RequestStatus status;

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
		hospitalName = csr.getHospitalName();
		id = csr.getId();
		certificateUser = csr.getCertificateUser();
	}

	public byte[] getCSRBytes() {
		String everything = commonName + organization + organizationUnit + locality + state + country + email + hospitalName + certificateUser;
		return everything.getBytes();
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
