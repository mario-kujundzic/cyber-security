package com.security.hospital.service;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.security.hospital.dto.DeleteUserRequestDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.model.Admin;
import com.security.hospital.model.User;
import com.security.hospital.model.requests.DeleteUserRequest;
import com.security.hospital.model.requests.RequestStatus;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;
import com.security.hospital.repository.DeleteUserRequestRepository;

@Service
public class DeleteUserRequestService {
	DeleteUserRequestRepository repository;
	UserService userService;
	RestTemplate restTemplate;
	String resourceFolderPath;

	@Autowired
	public DeleteUserRequestService(@Value("${server.ssl.key-store-folder}") String resourceFolderPath,
			DeleteUserRequestRepository repository, UserService userService, RestTemplate restTemplate) {
		this.repository = repository;
		this.userService = userService;
		this.resourceFolderPath = resourceFolderPath;
		this.restTemplate = restTemplate;
	}

	public GenericMessageDTO create(Long userId, Admin admin) throws IOException {
		User user = userService.getOneById(userId);
		DeleteUserRequest request = new DeleteUserRequest();
		request.setUsername(user.getUsername());
		request.setName(user.getName());
		request.setSurname(user.getSurname());
		request.setRole(user.getRole());
		request.setReason("Ne svidja mi se kako izgledas");
		request.setUserId(userId);
		request.setAdminId(admin.getId());
		request.setAdminEmail(admin.getUsername());
		request.setHospitalName("Hospital1");
		request.setStatus(RequestStatus.PENDING);

		request = repository.save(request);

		DeleteUserRequestDTO dto = new DeleteUserRequestDTO(request);

		// Add signature
		byte[] csrBytes = dto.getCSRBytes();
		PrivateKey privateKey = KeyPairUtility.readPrivateKey(resourceFolderPath + "/key.priv");
		byte[] signature = CryptographicUtility.sign(csrBytes, privateKey);
		String base64Signature = Base64.getEncoder().encodeToString(signature);
		dto.setSignature(base64Signature);

		String adminEndpointURI = "https://localhost:9001/api/deleteUserRequests/request";

		GenericMessageDTO csrResponse = this.restTemplate.postForObject(adminEndpointURI, dto, GenericMessageDTO.class);

		return csrResponse;
	}

	public List<DeleteUserRequestDTO> getAllRequestsDTO() {
		return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	private DeleteUserRequestDTO toDTO(DeleteUserRequest req) {
		return new DeleteUserRequestDTO(req);
	}

	public void processRequestResponse(@Valid DeleteUserRequestDTO dto) throws Exception {
		PublicKey rootCAKey = KeyPairUtility.readPublicKey(resourceFolderPath + "/rootCA.pub");

		if (rootCAKey == null) {
			throw new Exception(
					"Denied: Admin app public key not registered. Contact a super admin to obtain the public key.");
		}

		// Verify signature
		byte[] csrBytes = dto.getCSRBytes();
		byte[] signature = Base64.getDecoder().decode(dto.getSignature());
		boolean valid = CryptographicUtility.verify(csrBytes, signature, rootCAKey);

		if (!valid) {
			throw new Exception("Denied: signature invalid.");
		}

		DeleteUserRequest request = repository.getOne(dto.getId());
		if (!request.getStatus().equals(RequestStatus.PENDING)) {
			throw new Exception("Denied: potential replay attack - request already responded to!");
		}
		if (dto.getStatus().equals(RequestStatus.PENDING)) {
			throw new Exception("Denied: potential tampering in the response - request status not set!");
		}

		if (dto.getStatus().equals(RequestStatus.SIGNED)) {
			userService.deleteUser(dto);
		}

		request.setStatus(dto.getStatus());
		repository.save(request);
	}
}
