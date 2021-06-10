package com.security.hospital.dto;

import javax.validation.constraints.NotBlank;

import com.security.hospital.model.User;

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
	private String name;
	
	@NonNull
	@NotBlank(message = "Surname is required!")
	private String surname;

	@NonNull
	@NotBlank(message = "Username is required!")
	private String username;

	@NonNull
	@NotBlank(message = "Role is required!")
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
