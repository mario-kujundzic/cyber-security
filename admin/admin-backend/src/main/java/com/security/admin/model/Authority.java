package com.security.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "authorities")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Authority implements GrantedAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8590007026916695907L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", unique = true)
	@NonNull
	private String name;
	
	@Override
	public String getAuthority() {
		return this.name;
	}

}