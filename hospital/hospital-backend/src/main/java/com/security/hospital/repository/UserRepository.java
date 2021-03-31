package com.security.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
    User findByKey(String key);
    User findByResetKey(String resetKey);

}
