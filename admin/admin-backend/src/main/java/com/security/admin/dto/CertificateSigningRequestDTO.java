package com.security.admin.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CertificateSigningRequestDTO {
	
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
	private byte[] publicKey;
	
}
