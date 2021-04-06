package com.security.hospital.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenStateDTO {
	
	private Long id;
    @NotBlank(message = "Access token is required!")
    private String accessToken;
    private Long expiresIn;
    @NotBlank(message = "User role is required!")
    private String userRole;

}
