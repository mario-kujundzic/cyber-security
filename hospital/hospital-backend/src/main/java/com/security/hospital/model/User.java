package com.security.hospital.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING, length = 6)
@DiscriminatorOptions(force = true)
@Table(name = "users")
public abstract class User implements UserDetails {
	
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

	@Column(name = "role", insertable = false, updatable = false)
	private String role;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", 
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities;

	@Column(name = "last_password_reset_date")
	private Timestamp lastPasswordResetDate;
		
}
