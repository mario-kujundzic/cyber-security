package com.security.hospital.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.hospital.dto.PatientDTO;
import com.security.hospital.model.Patient;
import com.security.hospital.repository.PatientRepository;

@Service
public class PatientService {
	
	private PatientRepository patientRepository;
	
	@Autowired
	public PatientService(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}
	
	public List<PatientDTO> getAllPatients() {
		List<PatientDTO> dtos = new ArrayList<>();
		List<Patient> patients = patientRepository.findAll();
		for (Patient p : patients) {
			dtos.add(new PatientDTO(p));
		}
		return dtos;
	}
	
}
