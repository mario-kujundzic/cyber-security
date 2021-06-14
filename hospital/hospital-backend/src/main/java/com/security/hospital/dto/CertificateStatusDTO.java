package com.security.hospital.dto;

import com.security.hospital.enums.CertificateStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CertificateStatusDTO {
	
	@NonNull
	private String serialNumber;
	private CertificateStatus status;
	private String signature;
	
	
	public byte[] getCSRBytes() {
		String everything = String.join("", serialNumber, status.toString());
		return everything.getBytes();
	}
	
}
