package com.security.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.admin.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByKey(String key);

    User findByResetKey(String resetKey);
}
