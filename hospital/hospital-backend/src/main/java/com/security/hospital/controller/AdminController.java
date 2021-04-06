package com.security.hospital.controller;

import com.security.hospital.certificates.CertificateRequest;
import com.security.hospital.dto.CertificateSigningRequestDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/requestCertificate")
    public ResponseEntity<GenericMessageDTO> requestCertificate(@RequestBody CertificateRequest certificateRequest,
                                                                HttpServletResponse response) throws DisabledException{
        final String adminServerEndpoint = "http://localhost:9001/api/certificateRequests";
        RestTemplate restTemplate = new RestTemplate();

        CertificateSigningRequestDTO refinedDTO = new CertificateSigningRequestDTO(certificateRequest);

        GenericMessageDTO csrResponse = restTemplate.postForObject(adminServerEndpoint, certificateRequest, GenericMessageDTO.class);

        return new ResponseEntity<>(csrResponse, HttpStatus.OK);

    }
}
