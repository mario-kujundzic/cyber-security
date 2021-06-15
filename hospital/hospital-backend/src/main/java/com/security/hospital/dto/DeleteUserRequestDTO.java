package com.security.hospital.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.hospital.model.requests.DeleteUserRequest;
import com.security.hospital.model.requests.RequestStatus;
import com.security.hospital.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DeleteUserRequestDTO {
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
	@NotBlank(message = "Reason for deletion is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Reason for deletion must be alphanumeric!")
	private String reason;

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

	public DeleteUserRequestDTO(DeleteUserRequest dur) {
		username = dur.getUsername();
		name = dur.getName();
		surname = dur.getSurname();
		role = dur.getRole();
		reason = dur.getReason();
		adminEmail = dur.getAdminEmail();
		hospitalName = dur.getHospitalName();
		userId = dur.getUserId();
		adminId = dur.getAdminId();
		status = dur.getStatus();
		id = dur.getId();
	}

	public byte[] getCSRBytes() {
		String everything = String.join("", username, name, surname, role, reason, adminEmail, hospitalName,
				userId + "", adminId + "", status.toString());
		return everything.getBytes();
	}
}
