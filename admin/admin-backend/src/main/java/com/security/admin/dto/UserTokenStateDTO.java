package com.security.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserTokenStateDTO {
	
	private Long id;
//    @NotBlank(message = "Access token is required!")
    private String accessToken;
    private Long expiresIn;

    public UserTokenStateDTO(Long id, String accessToken, long expiresIn) {
    	this.id = id;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
    
}
