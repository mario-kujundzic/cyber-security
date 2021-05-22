package com.security.hospital.dto;

//import javax.validation.constraints.NotBlank;
import com.security.hospital.util.ValidationUtility;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class UserTokenStateDTO {
	
	private Long id;
//  @NotBlank(message = "Access token is required!")
	private String accessToken;
	private Long expiresIn;

	@Pattern(regexp = ValidationUtility.emailRegex, message = "Username must be a valid email address!")
	private String username;

	@Pattern(regexp = ValidationUtility.englishAlphabetRegex, message = "Name must be made only of English letters!")
	private String name;

	@Pattern(regexp = ValidationUtility.englishAlphabetRegex, message = "Surname must be made only of English letters!")
	private String surname;
	private String role;

	public UserTokenStateDTO(Long id, String accessToken, long expiresIn, String username, String name,
			String surname, String role) {
		this.id = id;
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.role = role;
	}

}
