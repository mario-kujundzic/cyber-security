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

import com.security.hospital.dto.AddUserRequestDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.dto.UserDTO;
import com.security.hospital.model.Admin;
import com.security.hospital.model.requests.AddUserRequest;
import com.security.hospital.model.requests.RequestStatus;
import com.security.hospital.pki.util.CryptographicUtility;
import com.security.hospital.pki.util.KeyPairUtility;
import com.security.hospital.repository.AddUserRequestRepository;

@Service
public class AddUserRequestService {

	AddUserRequestRepository repository;
	UserService userService;
	RestTemplate restTemplate;
	String resourceFolderPath;

	@Autowired
	public AddUserRequestService(@Value("${server.ssl.key-store-folder}") String resourceFolderPath,
			AddUserRequestRepository repository, UserService userService, RestTemplate restTemplate) {
		this.repository = repository;
		this.userService = userService;
		this.resourceFolderPath = resourceFolderPath;
		this.restTemplate = restTemplate;
	}

	public GenericMessageDTO create(UserDTO dto, Admin admin) throws IOException {
		AddUserRequest request = new AddUserRequest();
		request.setName(dto.getName());
		request.setSurname(dto.getSurname());
		request.setRole(dto.getRole());
		request.setUsername(dto.getUsername());
		request.setAdminEmail(admin.getUsername());
		request.setAdminId(admin.getId());
		request.setHospitalName("Hospital1");
		
		request.setStatus(RequestStatus.PENDING);
		
		request = repository.save(request);
		
		AddUserRequestDTO requestDTO = new AddUserRequestDTO(request);

		// Add signature
		byte[] csrBytes = requestDTO.getCSRBytes();
		PrivateKey privateKey = KeyPairUtility.readPrivateKey(resourceFolderPath + "/key.priv");
		byte[] signature = CryptographicUtility.sign(csrBytes, privateKey);
		String base64Signature = Base64.getEncoder().encodeToString(signature);
		requestDTO.setSignature(base64Signature);
		
		String adminEndpointURI = "https://localhost:9001/api/addUserRequests/request";
		
		GenericMessageDTO csrResponse = this.restTemplate.postForObject(adminEndpointURI, requestDTO,
				GenericMessageDTO.class);

		return csrResponse;
	}

	public List<AddUserRequestDTO> getAllRequestsDTO() {
		return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	private AddUserRequestDTO toDTO (AddUserRequest req) {
		return new AddUserRequestDTO(req);
	}

	public void processRequestResponse(@Valid AddUserRequestDTO dto) throws Exception {
		PublicKey rootCAKey = KeyPairUtility.readPublicKey(resourceFolderPath + "/rootCA.pub");

		if (rootCAKey == null) {
			throw new Exception("Denied: Admin app public key not registered. Contact a super admin to obtain the public key.");
		}

		// Verify signature
		byte[] csrBytes = dto.getCSRBytes();
		byte[] signature = Base64.getDecoder().decode(dto.getSignature());
		boolean valid = CryptographicUtility.verify(csrBytes, signature, rootCAKey);

		if (!valid) {
			throw new Exception("Denied: signature invalid.");
		}

		AddUserRequest request = repository.getOne(dto.getId());
		if (!request.getStatus().equals(RequestStatus.PENDING)) {
			throw new Exception("Denied: potential replay attack - request already responded to!");
		}
		if (dto.getStatus().equals(RequestStatus.PENDING)) {
			throw new Exception("Denied: potential tampering in the response - request status not set!");
		}
		
		if (dto.getStatus().equals(RequestStatus.SIGNED)) {
			userService.addUser(dto);
		}
		
		request.setStatus(dto.getStatus());
		repository.save(request);	
	}
}
