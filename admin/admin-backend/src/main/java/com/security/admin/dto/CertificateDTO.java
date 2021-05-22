package com.security.admin.dto;

import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.admin.util.ValidationUtility;
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
	private Long validFrom;
	
	@NonNull
	private Long validTo;
	
	@NonNull
	private List<Integer> purpose;

	@NonNull
	@NotBlank(message = "Algorithm is required!")
	@Pattern(regexp = ValidationUtility.englishStringRegex, message = "Algorithm must be made of alphanumeric characters (special characters and punctuation allowed)!")
	private String algorithm;
	
	@NonNull
	private Long requestId;
	
	// TODO: ne mora uvek biti rootCA, moze biti hijerahija
	// treba da moze da se menja issuer kasnije	
	private String issuer;
	
	private List<String> purposeReadable;
	
	private boolean revocationStatus;

	@Pattern(regexp = ValidationUtility.englishStringRegex, message = "Revocation reason must be made of alphanumeric characters (special characters and punctuation allowed)!")
	private String revocationReason;
	
	private boolean rootAuthority;
}
