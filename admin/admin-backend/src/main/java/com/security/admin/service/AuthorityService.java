package com.security.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.admin.model.Authority;
import com.security.admin.repository.AuthorityRepository;

@Service
public class AuthorityService {

    private AuthorityRepository authRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Authority findOne(long id) {
        return authRepository.getOne(id);
    }

    public Authority findByName(String name) {
        return authRepository.findByName(name);
    }
    
    public Authority create(String name) {
    	Authority role = new Authority(name);
    	return authRepository.save(role);
    }
}
