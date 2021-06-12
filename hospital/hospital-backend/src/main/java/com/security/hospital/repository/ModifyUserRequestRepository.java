package com.security.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.requests.ModifyUserRequest;
import com.security.hospital.model.requests.RequestStatus;

public interface ModifyUserRequestRepository extends JpaRepository<ModifyUserRequest, Long> {
	List<ModifyUserRequest> findAll();
	List<ModifyUserRequest> getAllByStatus(RequestStatus status);
	ModifyUserRequest getOneByIdAndStatus(long id, RequestStatus status);
}
