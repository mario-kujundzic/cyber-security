package com.security.hospital.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.hospital.model.User;

import com.security.hospital.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDTO {

	private Long id;
		
	@NonNull
	@NotBlank(message = "Name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Name must be alphanumeric!")
	private String name;
	
	@NonNull
	@NotBlank(message = "Surname is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Surname must be alphanumeric!")
	private String surname;

	@NonNull
	@NotBlank(message = "Username is required!")
	@Pattern(regexp = ValidationUtility.emailRegex, message = "User email must be in proper format!")
	private String username;

	@NonNull
	@NotBlank(message = "Role is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Role must be alphanumeric!")
	private String role;
	
	public UserDTO(User u) {
		if (u == null) {
			return;
		}
		
		id = u.getId();
		name = u.getName();
		surname = u.getSurname();
		username = u.getUsername();
		role = u.getRole();
	}
}
