package com.security.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.admin.model.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
	Role findByName(String name);
}