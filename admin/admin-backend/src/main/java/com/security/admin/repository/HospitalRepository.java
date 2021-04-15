package com.security.admin.repository;

import com.security.admin.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    public Hospital getByCommonName(String commonName);
}
