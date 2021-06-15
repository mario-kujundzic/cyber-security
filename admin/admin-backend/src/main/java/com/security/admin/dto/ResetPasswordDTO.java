package com.security.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.admin.util.ValidationUtility;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordDTO {
	
	@NotBlank(message = "Password is required!")
	@Pattern(regexp = ValidationUtility.passwordRegex, message="Password should contain at least one special character, one uppercase letter and one number")
    private String newPassword;
    
    @NotBlank(message = "Reset key is required!")
    private String resetKey;

}
