package com.security.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.admin.model.requests.AddUserRequest;
import com.security.admin.model.requests.RequestStatus;

public interface AddUserRequestRepository extends JpaRepository<AddUserRequest, Long> {
	List<AddUserRequest> findAll();
	List<AddUserRequest> getAllByStatus(RequestStatus status);
	AddUserRequest getOneByIdAndStatus(long id, RequestStatus status);
}