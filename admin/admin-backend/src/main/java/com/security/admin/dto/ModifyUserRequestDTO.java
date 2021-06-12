package com.security.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.admin.model.requests.ModifyUserRequest;
import com.security.admin.model.requests.RequestStatus;
import com.security.admin.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ModifyUserRequestDTO {
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
	@NotBlank(message = "Current role is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Current role must be alphanumeric!")
	private String currentRole;

	@NonNull
	@NotBlank(message = "New role is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "New role must be alphanumeric!")
	private String newRole;

	@NonNull
	@NotBlank(message = "Admin email is required!")
	@Pattern(regexp = ValidationUtility.emailRegex, message = "Admin email must be in proper format!")
	private String adminEmail;

	@NonNull
	@NotBlank(message = "Hospital name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Hospital name must be alphanumeric!")
	private String hospitalName;

	@NonNull
	private Long userId;

	@NonNull
	private Long adminId;

	@NonNull
	@NotBlank(message = "Signature is required!")
	@Pattern(regexp = ValidationUtility.base64Regex, message = "Signature should be a base64 string!")
	private String signature;

	private RequestStatus status;

	public ModifyUserRequestDTO(ModifyUserRequest entity) {
		username = entity.getUsername();
		name = entity.getName();
		surname = entity.getSurname();
		currentRole = entity.getCurrentRole();
		newRole = entity.getNewRole();
		adminEmail = entity.getAdminEmail();
		hospitalName = entity.getHospitalName();
		userId = entity.getUserId();
		adminId = entity.getAdminId();
		status = entity.getStatus();
		id = entity.getId();
	}

	public byte[] getCSRBytes() {
		String everything = String.join("", username, name, surname, currentRole, newRole, adminEmail,
				hospitalName, userId + "", adminId + "", status.toString());
		return everything.getBytes();
	}
}
