package com.security.hospital.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.hospital.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	
    @NotBlank(message = "Name is required!")
    private String name;
    
    @NotBlank(message = "Surname is required")
    private String surname;
    
    @NotBlank(message = "Username is required!")
    @Email(message = "Username should be your email address!")
    private String username;
    
    @NotBlank(message = "Password is required!")
	@Pattern(regexp = ValidationUtility.passwordRegex, message="Password should contain at least one special character, one uppercase letter and one number")
    private String password;
}
