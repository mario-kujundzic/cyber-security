package com.security.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.requests.AddUserRequest;
import com.security.hospital.model.requests.RequestStatus;


public interface AddUserRequestRepository extends JpaRepository<AddUserRequest, Long> {
	List<AddUserRequest> findAll();
	List<AddUserRequest> getAllByStatus(RequestStatus status);
	AddUserRequest getOneByIdAndStatus(long id, RequestStatus status);
}