package com.security.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class GenericMessageDTO {
    private String message;

    public GenericMessageDTO(String message) {
        this.message = message;
    }
}
