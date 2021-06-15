package com.security.hospital.dto;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.security.hospital.util.ValidationUtility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMessageDTO {
	@NonNull
	@NotBlank(message = "Device common name is required!")
	@Pattern(regexp = ValidationUtility.alphaNumericRegex, message = "Device common name must be alphanumeric!")
	private String commonName;

	private Map<String, String> parameters;	

	@NonNull
	@NotBlank(message = "Signature is required!")
	@Pattern(regexp = ValidationUtility.base64Regex, message = "Signature should be a base64 string!")
	private String signature;
	
    public byte[] getCSRBytes() {
        String everything = commonName;
        List<String> keys = parameters.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        for (String key : keys)
        	everything = everything + key;
        return everything.getBytes();
    }
}
