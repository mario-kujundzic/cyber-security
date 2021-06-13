package com.security.admin.service;

import com.security.admin.dto.CertificateSigningRequestDTO;
import com.security.admin.dto.HospitalDTO;
import com.security.admin.model.requests.CertificateSigningRequest;
import com.security.admin.model.requests.RequestStatus;
import com.security.admin.pki.util.CryptographicUtility;
import com.security.admin.pki.util.PEMUtility;
import com.security.admin.repository.CertificateSigningRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CertificateSigningRequestService {
	@Autowired
	private CertificateSigningRequestRepository repository;

	@Autowired
	private HospitalService hospitalService;

	public void addRequest(CertificateSigningRequestDTO dto) throws Exception {
		// Look up public key of hospital
		String hospitalName = dto.getHospitalName();
		HospitalDTO hospitalDTO = hospitalService.getByCommonName(hospitalName);
		String publicKeyPEM = hospitalDTO.getPublicKey();

		if (publicKeyPEM == null) {
			throw new Exception("Denied: Hospital with common name " + hospitalName + " not found. Contact a super admin to register this hospital's public key.");
		}

		// Verify signature
		byte[] csrBytes = dto.getCSRBytes();
		byte[] signature = Base64.getDecoder().decode(dto.getSignature());
		PublicKey publicKey = PEMUtility.PEMToPublicKey(publicKeyPEM);
		boolean valid = CryptographicUtility.verify(csrBytes, signature, publicKey);

		if (!valid) {
			throw new Exception("Denied: signature invalid.");
		}

		CertificateSigningRequest request = new CertificateSigningRequest(dto);

		save(request);
	}
	
	public void save(CertificateSigningRequest req) {
		repository.save(req);
	}

	public CertificateSigningRequestDTO getUnsignedRequestDTO(long id) {
		return toDTO(repository.getOneByIdAndStatus(id, RequestStatus.PENDING));
	}

	public List<CertificateSigningRequestDTO> getAllRequestsDTO() {
		ArrayList<CertificateSigningRequestDTO> list = new ArrayList<>();

		for (CertificateSigningRequest csr : repository.findAll()) {
			list.add(new CertificateSigningRequestDTO(csr));
		}

		return list;
	}

	private CertificateSigningRequestDTO toDTO(CertificateSigningRequest req) {
		CertificateSigningRequestDTO dto = new CertificateSigningRequestDTO(req);
		return dto;
	}

	public CertificateSigningRequest getOne(Long requestId) {
		return repository.getOne(requestId);
	}

	public void declineRequest(long id) {
		CertificateSigningRequest request = repository.getOne(id);
		request.setStatus(RequestStatus.REJECTED);
		repository.save(request);
	}
}