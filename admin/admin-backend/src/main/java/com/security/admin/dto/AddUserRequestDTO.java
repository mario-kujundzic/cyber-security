package com.security.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.admin.model.requests.AddUserRequest;
import com.security.admin.model.requests.RequestStatus;
import com.security.admin.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AddUserRequestDTO {
	private Long id;

	@NonNull
	@NotBlank(message = "Username is required!")
	@Pattern(regexp = ValidationUtility.emailRegex, message = "User email must be in proper format!")
	private String username;

	@NonNull
	@NotBlank(message = "Name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "User name must be alphanumeric!")
	private String name;

	@NonNull
	@NotBlank(message = "Surname is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "User surname must be alphanumeric!")
	private String surname;

	@NonNull
	@NotBlank(message = "Role is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Role must be alphanumeric!")
	private String role;

	@NonNull
	@NotBlank(message = "Admin email is required!")
	@Pattern(regexp = ValidationUtility.emailRegex, message = "Admin email must be in proper format!")
	private String adminEmail;

	@NonNull
	@NotBlank(message = "Hospital name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Hospital name must be alphanumeric!")
	private String hospitalName;

	@NonNull
	private Long adminId;

	@NonNull
	@NotBlank(message = "Signature is required!")
	@Pattern(regexp = ValidationUtility.base64Regex, message = "Signature should be a base64 string!")
	private String signature;

	private RequestStatus status;

	public AddUserRequestDTO(AddUserRequest entity) {
		username = entity.getUsername();
		name = entity.getName();
		surname = entity.getSurname();
		role = entity.getRole();
		adminEmail = entity.getAdminEmail();
		hospitalName = entity.getHospitalName();
		adminId = entity.getAdminId();
		status = entity.getStatus();
		id = entity.getId();
	}

	public byte[] getCSRBytes() {
		String everything = String.join("", username, name, surname, role, adminEmail,
				hospitalName, adminId + "", status.toString());
		return everything.getBytes();
	}
}
