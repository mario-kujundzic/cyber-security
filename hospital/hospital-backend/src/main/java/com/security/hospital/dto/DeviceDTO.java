package com.security.hospital.dto;

import javax.validation.constraints.NotBlank;

import com.security.hospital.model.Device;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DeviceDTO {

	private Long id;
		
	@NonNull
	@NotBlank(message = "Common Name is required!")
	private String commonName;

	@NonNull
	@NotBlank(message = "Public key is required!")
	private String publicKey;

	public DeviceDTO(Device device) {
		if (device == null) {
			return;
		}

		id = device.getId();
		commonName = device.getCommonName();
		publicKey = device.getPublicKey();
	}

    public byte[] getCSRBytes() {
        String everything = commonName;
        return everything.getBytes();
    }
}
