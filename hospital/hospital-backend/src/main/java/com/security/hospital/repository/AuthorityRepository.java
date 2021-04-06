package com.security.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(String name);
    List<Authority> findAll();
}
