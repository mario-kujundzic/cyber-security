package com.security.hospital.certificates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

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

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getCityLocality() {
        return cityLocality;
    }

    public void setCityLocality(String cityLocality) {
        this.cityLocality = cityLocality;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountryRegion() {
        return countryRegion;
    }

    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
