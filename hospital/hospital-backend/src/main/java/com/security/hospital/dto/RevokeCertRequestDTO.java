package com.security.hospital.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.hospital.model.requests.RequestStatus;
import com.security.hospital.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class RevokeCertRequestDTO {
	@NonNull
	@NotBlank(message = "Certificate id is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Certificate id must be alphanumeric!")
	private String certificateId;

	@NonNull
	@NotBlank(message = "Revocation reason is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Revocation reason must be alphanumeric!")
	private String revocationReason;

	@NonNull
	@NotBlank(message = "Signature is required!")
	@Pattern(regexp = ValidationUtility.base64Regex, message = "Signature should be a base64 string!")
	private String signature;

	private RequestStatus status;

	public byte[] getCSRBytes() {
		String everything = String.join("", certificateId, revocationReason, status.toString());
		return everything.getBytes();
	}
}
