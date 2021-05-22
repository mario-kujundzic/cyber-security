package com.security.hospital.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.security.hospital.dto.DeviceDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
public class Device {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(name = "common_name")
	private String commonName;

	@Column(name = "public_key", columnDefinition = "TEXT")
	private String publicKey;

	public Device(DeviceDTO dto) {
		id = dto.getId();
		commonName = dto.getCommonName();
		publicKey = dto.getPublicKey();
	}

}