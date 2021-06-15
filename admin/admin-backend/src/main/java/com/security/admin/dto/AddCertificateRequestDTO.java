package com.security.admin.dto;

import java.math.BigInteger;

import com.security.admin.model.CertificateUser;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCertificateRequestDTO {
	
	private BigInteger serialNumber;
	private Long validFrom;
	private Long validTo;
	private String email;
	private String commonName;
	private CertificateUser certificateUser;
	private String signature;
	
	public byte[] getCSRBytes() {
		String everything = String.join("", serialNumber.toString(), 
				validFrom.toString(), validTo.toString(), email, commonName, certificateUser.toString());
		return everything.getBytes();
	}
	
	
}