package com.security.hospital.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "patients")
@NoArgsConstructor
@Data
public class Patient {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "surname")
	private String surname;
	
	@Column(name = "jmbg")
	private String jmbg;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@Column(name="height")
	private float height;
	
	@Column(name="weight")
	private float weight;
	
	@Column(name="blood_type")
	private String bloodType;
	
	@Column(name="blood_pressure")
	private String bloodPressure;
	
	@Column(name="illness")
	private String illness;
	
	@Column(name="vaccination")
	private String vaccination;
	
}
