package com.security.hospital.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.security.hospital.model.Log;

public interface CyberLogRepository extends MongoRepository<Log, Long>{

}
