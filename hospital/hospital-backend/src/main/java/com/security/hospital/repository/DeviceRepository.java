package com.security.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.hospital.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Long>{
    public Device getByCommonName(String commonName);
}
