package com.security.hospital.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
public class Admin extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5454352795022425690L;
	
	public Admin(String name, String surname, String username, String password) {
        super(name, surname, username, password);
    }

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
