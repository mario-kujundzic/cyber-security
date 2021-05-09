package com.security.admin.model;

import com.security.admin.dto.HospitalDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "hospitals")
@Data
@NoArgsConstructor
public class Hospital {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(name = "common_name")
	private String commonName;

	@Column(name = "public_key", columnDefinition = "TEXT")
	private String publicKey;

	public Hospital(HospitalDTO dto) {
		id = dto.getId();
		commonName = dto.getCommonName();
		publicKey = dto.getPublicKey();
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
