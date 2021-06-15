package com.security.admin.dto;

import com.security.admin.enums.CertificateStatus;

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
	private String revocationReason;
	private String signature;
	
	
	public byte[] getCSRBytes() {
		String everything = String.join("", serialNumber, status.toString(), revocationReason);
		return everything.getBytes();
	}
	
}
