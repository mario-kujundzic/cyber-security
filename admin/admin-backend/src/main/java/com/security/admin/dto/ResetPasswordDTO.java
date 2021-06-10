package com.security.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordDTO {
	
	@NotBlank(message = "Password is required!")
    @Size(min = 5, message = "Password must be at least 5 characters!")
    private String newPassword;
    
    @NotBlank(message = "Reset key is required!")
    private String resetKey;

}
