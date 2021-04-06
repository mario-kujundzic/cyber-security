package com.security.admin.dto;

import java.math.BigInteger;
import java.util.List;

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
public class CertificateDTO {
		
	private BigInteger serialNumber;
		
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
	@NotBlank(message = "Start date is required!")
	private Long validFrom;
	
	@NonNull
	@NotBlank(message = "End date is required!")
	private Long validTo;
	
	@NonNull
	@NotBlank(message = "Purpose is required!")
	private List<Integer> purpose;

	@NonNull
	@NotBlank(message = "Algorithm is required!")
	private String algorithm;
	
	@NonNull
	@NotBlank(message = "Certificate request ID is required!")
	private Long requestId;
	
	// TODO: ne mora uvek biti rootCA, moze biti hijerahija
	// treba da moze da se menja issuer kasnije	
	private String issuer;
	
	private List<String> purposeReadable;
	
	private boolean revocationStatus;
	
	private boolean rootAuthority;
}
