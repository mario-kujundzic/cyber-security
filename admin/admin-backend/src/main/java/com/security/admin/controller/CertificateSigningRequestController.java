package com.security.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.admin.dto.CertificateSigningRequestDTO;
import com.security.admin.dto.GenericMessageDTO;
import com.security.admin.model.CertificateUser;
import com.security.admin.service.CertificateService;
import com.security.admin.service.CertificateSigningRequestService;

@RestController
@RequestMapping(value = "/api/certificateRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateSigningRequestController {
    private CertificateSigningRequestService certificateSigningRequestService;

    private CertificateService certService;
    
    @Autowired
    public CertificateSigningRequestController(CertificateSigningRequestService certificateRequestService, CertificateService certService) {
        this.certificateSigningRequestService = certificateRequestService;
        this.certService = certService;
    }

    @PostMapping("/request")
    public ResponseEntity<Object> requestNewCertificate(@RequestBody @Valid CertificateSigningRequestDTO dto, HttpServletRequest request) {
        try {
        	if (dto.getCertificateUser().equals(CertificateUser.DEVICE))
        		this.certService.checkCertificateFromRequest(request);
            certificateSigningRequestService.addRequest(dto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new GenericMessageDTO("Certificate request successfully created! Wait for the Super Admin to respond."), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<CertificateSigningRequestDTO> getCertificate(@PathVariable long id) {
    	CertificateSigningRequestDTO dto = certificateSigningRequestService.getUnsignedRequestDTO(id);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<List<CertificateSigningRequestDTO>> getAllCertificateRequests() {
        List<CertificateSigningRequestDTO> dtos = certificateSigningRequestService.getAllRequestsDTO();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    
    @GetMapping("/decline/{id}")
    @PreAuthorize("hasAuthority('UPDATE_PRIVILEGE')")
    public ResponseEntity<Object> declineCertificateRequest(@PathVariable long id) {
    	certificateSigningRequestService.declineRequest(id);
    	return new ResponseEntity<>("Successfully declined request", HttpStatus.OK);
    }
}
