package com.security.hospital.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.hospital.model.requests.CertificateUser;
import com.security.hospital.util.ValidationUtility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificateRequestDTO {

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
	
    private String publicKey;

    private String signature;
	
	private CertificateUser certificateUser;

    public byte[] getCSRBytes() {
        String everything = commonName + organization + organizationUnit + locality + state + country + email + hospitalName + certificateUser;
        return everything.getBytes();
    }
}
