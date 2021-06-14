package com.security.hospital.dto;

import java.math.BigInteger;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.hospital.model.requests.CertificateUser;
import com.security.hospital.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCertificateRequestDTO {
	
	private BigInteger serialNumber;
	
	private Long validFrom;
	
	private Long validTo;
	
	@Pattern(regexp = ValidationUtility.emailRegex, message = "Email should be in valid format!")
	private String email;
	
	@NotBlank(message = "Common Name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Common name must be alphanumeric!")
	private String commonName;
	
	@NotBlank(message = "Signature is required!")
	private String signature;
	
	private CertificateUser certificateUser;
	
	public byte[] getCSRBytes() {
		String everything = String.join("", serialNumber.toString(), 
				validFrom.toString(), validTo.toString(), email, commonName, certificateUser.toString());
		return everything.getBytes();
	}

}
