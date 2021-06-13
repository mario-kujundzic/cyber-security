package com.security.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.admin.model.requests.RequestStatus;
import com.security.admin.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class RevokeCertRequestDTO {
	@NonNull
	@NotBlank(message = "Certificate serial number is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Certificate serial number must be alphanumeric!")
	private String serialNumber;

	@NonNull
	@NotBlank(message = "Revocation reason is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Revocation reason must be alphanumeric!")
	private String revocationReason;

	@NonNull
	@NotBlank(message = "Hospital name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Hospital name must be alphanumeric!")
	private String hospitalName;

	@NonNull
	@NotBlank(message = "Signature is required!")
	@Pattern(regexp = ValidationUtility.base64Regex, message = "Signature should be a base64 string!")
	private String signature;

	private RequestStatus status;

	public byte[] getCSRBytes() {
		String everything = String.join("", serialNumber, revocationReason, hospitalName, status.toString());
		return everything.getBytes();
	}
}
