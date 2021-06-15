package com.security.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
