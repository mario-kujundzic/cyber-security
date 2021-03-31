package com.security.hospital.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("DOCTOR")
public class Doctor extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3998339517700186999L;
	
	public Doctor(String name, String surname, String username, String password) {
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
