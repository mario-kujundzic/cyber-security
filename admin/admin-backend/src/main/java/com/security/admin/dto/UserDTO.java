package com.security.admin.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(min = 5, message = "Password must be at least 5 characters!")
    private String password;
}
