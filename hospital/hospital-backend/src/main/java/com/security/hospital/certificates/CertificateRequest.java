package com.security.hospital.certificates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequest {
    @NotBlank(message = "Common name is required!")
    private String commonName;
    @NotBlank(message = "Organization is required!")
    private String organization;
    @NotBlank(message = "Organization unit is required!")
    private String organizationUnit;
    @NotBlank(message = "City/Locality is required!")
    private String cityLocality;
    @NotBlank(message = "State/Province is required!")
    private String stateProvince;
    @NotBlank(message = "Country/Region is required!")
    private String countryRegion;
    @NotBlank(message = "Public key is required!")
    private String publicKey;
}
