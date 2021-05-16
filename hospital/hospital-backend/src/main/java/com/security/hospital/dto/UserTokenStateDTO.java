package com.security.hospital.dto;

//import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTokenStateDTO {
	
	private Long id;
//  @NotBlank(message = "Access token is required!")
	private String accessToken;
	private Long expiresIn;
	private String username;
	private String name;
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
