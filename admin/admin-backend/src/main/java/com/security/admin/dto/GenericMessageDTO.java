package com.security.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class GenericMessageDTO {

	private String message;

    public GenericMessageDTO(String message) {
    	this.message = message;
    }
    
}
