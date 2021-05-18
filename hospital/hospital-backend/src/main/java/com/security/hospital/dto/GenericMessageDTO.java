package com.security.hospital.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class GenericMessageDTO {
    private String message;

    public GenericMessageDTO(String message) {
        this.message = message;
    }
}
