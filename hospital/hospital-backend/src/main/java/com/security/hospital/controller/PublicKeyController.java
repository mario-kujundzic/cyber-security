package com.security.hospital.controller;

import com.security.hospital.FirstTimeSetup;
import com.security.hospital.certificates.CertificateRequest;
import com.security.hospital.dto.GenericMessageDTO;
import com.security.hospital.model.Admin;
import com.security.hospital.pki.keystore.KeyStoreManager;
import com.security.hospital.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping("/api/publicKey")
public class PublicKeyController {

    private static String keyStoreFolderPath;

    private String publicKey;

    @Autowired
    public PublicKeyController(@Value("${server.ssl.key-store-folder}") String keyStoreFolderPath) {
        PublicKeyController.keyStoreFolderPath = keyStoreFolderPath;
        publicKey = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(keyStoreFolderPath + "/key.pub"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                publicKey += line;
            }
        } catch (Exception e) {
            System.out.println("PublicKeyController: Can't read public key! Error message is " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getPublicKey() {
        return new ResponseEntity<>(this.publicKey, HttpStatus.OK);
    }
}
