package com.security.hospital.service;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.hospital.dto.CertificateRequestDTO;
import com.security.hospital.dto.DeviceDTO;
import com.security.hospital.dto.DeviceMessageDTO;
import com.security.hospital.model.Device;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.PEMUtility;
import com.security.hospital.repository.DeviceRepository;


@Service
public class DeviceService {
	@Autowired
    private DeviceRepository deviceRepository;


    public DeviceDTO getOne(long id) {
        Device device = deviceRepository.getOne(id);

        return new DeviceDTO(device);
    }

    public DeviceDTO getByCommonName(String commonName) {
        Device device = deviceRepository.getByCommonName(commonName);

        return new DeviceDTO(device);
    }

    public ArrayList<DeviceDTO> getAll() {
        ArrayList<DeviceDTO> list = new ArrayList<>();

        List<Device> devices = deviceRepository.findAll();

        for (Device device : devices) {
            list.add(new DeviceDTO(device));
        }

        return list;
    }

    public DeviceDTO create(DeviceDTO dto) {
        Device device = new Device(dto);

        device = deviceRepository.save(device);
        dto = new DeviceDTO(device);
        return dto;
    }

    public DeviceDTO update(DeviceDTO dto) {
        Device device = new Device(dto);

        deviceRepository.save(device);

        device = deviceRepository.getOne(dto.getId());
        dto = new DeviceDTO(device);
        return dto;
    }

    public void delete(long id) {
    	deviceRepository.deleteById(id);
    }

	public void requestCertificate(CertificateRequestDTO dto) throws Exception {
		// Look up public key of hospital
		String commonName = dto.getCommonName();
		Device device = deviceRepository.getByCommonName(commonName);
		String publicKeyPEM = device.getPublicKey();

		if (publicKeyPEM == null) {
			throw new Exception("Denied: Device with common name " + commonName + " not found. Contact a hospital admin to register this device's public key.");
		}

		// Verify signature
		byte[] csrBytes = dto.getCSRBytes();
		byte[] signature = Base64.getDecoder().decode(dto.getSignature());
		PublicKey publicKey = PEMUtility.PEMToPublicKey(publicKeyPEM);
		boolean valid = CryptographicUtility.verify(csrBytes, signature, publicKey);

		if (!valid) {
			throw new Exception("Denied: Signature invalid.");
		}
		System.out.println("Prosao sve i sad treba da se posalje zahtev");
	}

	public void processMessage(DeviceMessageDTO dto) {
		// neka logika za proveru sertifikata
		System.out.println("Success!");		
	}
}
