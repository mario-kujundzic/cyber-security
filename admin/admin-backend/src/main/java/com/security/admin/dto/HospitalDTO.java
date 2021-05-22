package com.security.admin.dto;

import com.security.admin.model.Hospital;
import com.security.admin.util.ValidationUtility;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class HospitalDTO {
		
	private Long id;
		
	@NonNull
	@NotBlank(message = "Common Name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Common name must be alphanumeric!")
	private String commonName;

	@NonNull
	@NotBlank(message = "Public key is required!")
	@Pattern(regexp = ValidationUtility.PEMRegex, message = "Public key should be in PEM format!")
	private String publicKey;

	public HospitalDTO(Hospital hospital) {
		if (hospital == null) {
			return;
		}

		id = hospital.getId();
		commonName = hospital.getCommonName();
		publicKey = hospital.getPublicKey();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}
