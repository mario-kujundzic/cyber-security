package com.security.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
	Role findByName(String name);
}