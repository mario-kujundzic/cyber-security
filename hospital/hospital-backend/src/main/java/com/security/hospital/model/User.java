package com.security.hospital.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6819005769580886640L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NonNull
	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@NonNull
	@Column(name = "surname", unique = false, nullable = false)
	private String surname;
	
	@NonNull
	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	@NonNull
	@Column(name = "password", unique = false, nullable = false)
	private String password;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "key")
	private String key;
	
	@Column(name="reset_key")
	private String resetKey;

	@Column(name = "role", unique = false, nullable = false)
	private String role;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	protected List<Role> roles;

	@Column(name = "last_password_reset_date")
	private Timestamp lastPasswordResetDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// always one role
		if (!this.roles.isEmpty()) {
			Role role = this.roles.iterator().next();
			List<Privilege> privileges = new ArrayList<Privilege>();
			for (Privilege p : role.getPrivileges()) {
				privileges.add(p);
			}
			return privileges;
		}
		return null;
	}

	public Timestamp getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
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
