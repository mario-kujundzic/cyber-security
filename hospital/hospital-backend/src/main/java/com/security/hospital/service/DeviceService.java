package com.security.hospital.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.security.hospital.enums.LogMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.security.hospital.dto.CertificateRequestDTO;
import com.security.hospital.dto.DeviceDTO;
import com.security.hospital.dto.DeviceMessageDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.model.Device;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;
import com.security.hospital.pki.util.PEMUtility;
import com.security.hospital.repository.DeviceRepository;

@Service
public class DeviceService {
	private DeviceRepository deviceRepository;
	private String resourceFolderPath;
	private RestTemplate restTemplate;

	@Autowired
	private SecurityEventService securityEventService;

	@Autowired
	private LogService logService;

	@Autowired
	public DeviceService(DeviceRepository deviceRepository,
			@Value("${server.ssl.key-store-folder}") String resourceFolderPath, RestTemplate restTemplate) {
		this.deviceRepository = deviceRepository;
		this.resourceFolderPath = resourceFolderPath;
		this.restTemplate = restTemplate;
	}

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

	public GenericMessageDTO requestCertificate(CertificateRequestDTO dto) throws Exception {
		// Look up public key of hospital
		String commonName = dto.getCommonName();
		Device device = deviceRepository.getByCommonName(commonName);
		String publicKeyPEM = device.getPublicKey();

		if (publicKeyPEM == null) {
			throw new Exception("Denied: Device with common name " + commonName
					+ " not found. Contact a hospital admin to register this device's public key.");
		}

		// Verify signature
		byte[] csrBytes = dto.getCSRBytes();
		byte[] signature = Base64.getDecoder().decode(dto.getSignature());
		PublicKey publicKey = PEMUtility.PEMToPublicKey(publicKeyPEM);
		boolean valid = CryptographicUtility.verify(csrBytes, signature, publicKey);

		if (!valid) {
			throw new Exception("Denied: Signature invalid.");
		}

		String publicKeyHospitalPEM = KeyPairUtility.readPEM(resourceFolderPath + "/key.pub");
		dto.setPublicKey(publicKeyPEM);

		dto.setCommonName("Hospital1");

		// Add signature
		csrBytes = dto.getCSRBytes();
		PrivateKey privateKey = KeyPairUtility.readPrivateKey(resourceFolderPath + "/key.priv");
		signature = CryptographicUtility.sign(csrBytes, privateKey);
		String base64Signature = Base64.getEncoder().encodeToString(signature);
		dto.setSignature(base64Signature);

		GenericMessageDTO csrResponse;

		csrResponse = restTemplate.postForObject("http://localhost:9001/api/certificateRequests", dto,
				GenericMessageDTO.class);

		return csrResponse;
	}

	public void processMessage(DeviceMessageDTO dto) {
		// neka logika za proveru sertifikata
		String message = dto.getMessage();

		logService.logInfo("Device: " + message);
	}
}
