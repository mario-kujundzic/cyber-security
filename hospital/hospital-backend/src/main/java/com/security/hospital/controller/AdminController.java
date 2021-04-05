package com.security.hospital.controller;

import com.security.hospital.certificates.CertificateRequest;
import com.security.hospital.dto.MessageResponseDTO;
import com.security.hospital.dto.UserTokenStateDTO;
import com.security.hospital.exceptions.UserException;
import com.security.hospital.security.auth.JwtAuthenticationRequest;
import com.security.hospital.service.AdminService;
import com.security.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<MessageResponseDTO> requestCertificate(@RequestBody CertificateRequest certificateRequest,
                                                                 HttpServletResponse response) throws DisabledException{
        // TODO: contact admin application to create a CSR

        return new ResponseEntity<>(new MessageResponseDTO("Certificate signing request successfully created!"), HttpStatus.OK);

    }
}
