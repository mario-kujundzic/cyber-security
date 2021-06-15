package com.security.hospital.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.security.hospital.model.Log;

public interface CyberLogRepository extends MongoRepository<Log, Long>{
	List<Log> findAllByTimestampBetween(Date timestampStart, Date timestampEnd);
}
