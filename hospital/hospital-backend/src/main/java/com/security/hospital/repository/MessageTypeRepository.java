package com.security.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.devices.MessageType;

public interface MessageTypeRepository extends JpaRepository<MessageType, Long> {
	public MessageType getByParamName(String paramName);
}
