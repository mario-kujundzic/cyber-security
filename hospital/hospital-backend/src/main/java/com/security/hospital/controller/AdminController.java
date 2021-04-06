package com.security.hospital.controller;

import com.security.hospital.certificates.CertificateRequest;
import com.security.hospital.dto.RefinedCertificateSigningRequestDTO;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.model.Admin;
import com.security.hospital.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/requestCertificate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericMessageDTO> requestCertificate(@RequestBody CertificateRequest certificateRequest,
                                                                HttpServletResponse response, @AuthenticationPrincipal Admin admin) throws DisabledException{
        GenericMessageDTO csrResponse;
        try {
            csrResponse = adminService.makeCertificateRequest(
                    certificateRequest,
                    "http://localhost:9001/api/certificateRequests",
                    admin);
        } catch (IOException exception) {
            return new ResponseEntity<>(new GenericMessageDTO("Something went wrong while trying to read the public key."), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException exception) {
            return new ResponseEntity<>(new GenericMessageDTO("Something went wrong with the admin server, message: " + exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(csrResponse, HttpStatus.OK);

    }
}
