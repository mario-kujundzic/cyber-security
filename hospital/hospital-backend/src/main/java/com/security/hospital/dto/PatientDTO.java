package com.security.hospital.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.security.hospital.model.Patient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatientDTO {

	private long id;

	@NotBlank(message = "Name is required!")
	private String name;

	@NotBlank(message = "Surname is required!")
	private String surname;

	@NotBlank(message = "JMBG is required!")
	private String jmbg;

	@NotBlank(message = "Gender is required!")
	private String gender;

	@NotBlank(message = "Date of birth is required!")
	private Date dateOfBirth;

	@NotBlank(message = "Height is required!")
	private float height;

	@NotBlank(message = "Weight is required!")
	private float weight;

	@NotBlank(message = "Blood type is required!")
	private String bloodType;
	
	@NotBlank(message = "Blood pressure is required!")
	private String bloodPressure;
	
	@NotBlank(message = "Illness is required!")
	private String illness;
	
	@NotBlank(message = "Vaccination is required!")
	private String vaccination;

	
	public PatientDTO(Patient patient) {
		this.id = patient.getId();
		this.bloodType = patient.getBloodType();
		this.gender = patient.getGender();
		this.height = patient.getHeight();
		this.weight = patient.getWeight();
		this.jmbg = patient.getJmbg();
		this.name = patient.getName();
		this.surname = patient.getSurname();
		this.dateOfBirth = patient.getDateOfBirth();
		this.bloodPressure = patient.getBloodPressure();
		this.illness = patient.getIllness();
		this.vaccination = patient.getVaccination();
	}

}
