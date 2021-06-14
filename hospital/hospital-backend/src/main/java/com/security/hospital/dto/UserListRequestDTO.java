package com.security.hospital.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.hospital.util.RandomUtility;
import com.security.hospital.util.ValidationUtility;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserListRequestDTO {
	@NonNull
	@NotBlank(message = "Secure string is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Secure string must be alphanumeric!")
	private String secureString;

	@NonNull
	@NotBlank(message = "Signature is required!")
	@Pattern(regexp = ValidationUtility.base64Regex, message = "Signature should be a base64 string!")
	private String signature;
	
	public UserListRequestDTO() {
		secureString = RandomUtility.buildAuthString(20);
	}
	
	public byte[] getCSRBytes() {
		return secureString.getBytes();
	}
}
