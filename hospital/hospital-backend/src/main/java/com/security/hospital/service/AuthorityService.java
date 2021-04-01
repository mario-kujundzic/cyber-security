package com.security.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.hospital.model.Authority;
import com.security.hospital.repository.AuthorityRepository;


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
