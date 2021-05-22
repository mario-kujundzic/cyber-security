package com.security.hospital.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.hospital.dto.CertificateRequestDTO;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    @PreAuthorize("hasAuthority('CREATE_PRIVILEGE')")
    public ResponseEntity<GenericMessageDTO> requestCertificate(@RequestBody @Valid CertificateRequestDTO certificateRequest,
                                                                HttpServletResponse response, @AuthenticationPrincipal Admin admin) throws JsonProcessingException {

        GenericMessageDTO csrResponse;

        try {
            csrResponse = adminService.makeCertificateRequest(certificateRequest,
                    "https://localhost:9001/api/certificateRequests/request", admin);
        } catch (IOException exception) {
            return new ResponseEntity<>(new GenericMessageDTO("Something went wrong while trying to read the public key."), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException e) {
            String responseString = e.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            GenericMessageDTO messageDTO = objectMapper.readValue(responseString, GenericMessageDTO.class);
            return new ResponseEntity<>(messageDTO, HttpStatus.BAD_REQUEST);
        } catch (RestClientException e) {
            return new ResponseEntity<>(new GenericMessageDTO("Something went wrong with the admin server: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
        	System.out.println("Nesto jos je poslo po zlu, super xd");
            return new ResponseEntity<>(new GenericMessageDTO("Something went wrong with the admin server: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }



        return new ResponseEntity<>(csrResponse, HttpStatus.OK);

    }
}
