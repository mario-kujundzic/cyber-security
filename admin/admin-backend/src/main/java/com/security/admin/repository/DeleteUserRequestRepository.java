package com.security.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.admin.model.requests.DeleteUserRequest;
import com.security.admin.model.requests.RequestStatus;

public interface DeleteUserRequestRepository extends JpaRepository<DeleteUserRequest, Long> {
	List<DeleteUserRequest> findAll();
	List<DeleteUserRequest> getAllByStatus(RequestStatus status);
//	DeleteUserRequest getOneByCommonName(String commonName);
	DeleteUserRequest getOneByIdAndStatus(long id, RequestStatus status);
}
