package com.security.hospital.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericMessageDTO {
    private String message;

    public GenericMessageDTO(String message) {
        this.message = message;
    }
}
