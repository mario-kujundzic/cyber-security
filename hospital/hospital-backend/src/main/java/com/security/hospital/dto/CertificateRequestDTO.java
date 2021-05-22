package com.security.hospital.dto;

import com.security.hospital.util.ValidationUtility;
import lombok.*;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequestDTO {

    @NonNull
    @NotBlank(message = "Common Name is required!")
    @Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Common name must be alphanumeric!")
    private String commonName;

    @NonNull
    @NotBlank(message = "Organization is required!")
    @Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Organization must be made of alphanumeric characters!")
    private String organization;

    @NonNull
    @NotBlank(message = "Organizaton Unit is required!")
    @Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Organization unit must be made of alphanumeric characters!")
    private String organizationUnit;

    @NonNull
    @NotBlank(message = "Locality is required!")
    @Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Locality must be made of alphanumeric characters!")
    private String locality;

    @NonNull
    @NotBlank(message = "State is required!")
    @Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "State must be made of alphanumeric characters!")
    private String state;

    @NonNull
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be two-letter word")
    private String country;

    @NonNull
    @Pattern(regexp = ValidationUtility.emailRegex, message = "Email should be in valid format!")
    private String email;

    @Pattern(regexp = ValidationUtility.PEMRegex, message = "Public key should be in PEM format!")
    private String publicKey;

    @Pattern(regexp = ValidationUtility.base64Regex, message = "Signature should be a base64 string!")
    private String signature;

    public byte[] getCSRBytes() {
        String everything = commonName + organization + organizationUnit + locality + state + country + email;
        return everything.getBytes();
    }

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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
