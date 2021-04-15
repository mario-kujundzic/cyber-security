package com.security.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;

@RestController
@RequestMapping("/api/publicKey")
public class PublicKeyController {

    private static String keyStoreFolderPath;

    private String publicKey;

    @Autowired
    public PublicKeyController(@Value("${server.ssl.key-store-folder}") String keyStoreFolderPath) {
        PublicKeyController.keyStoreFolderPath = keyStoreFolderPath;
    }

    private String readPublicKey() {
        String publicKey = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(keyStoreFolderPath + "/key.pub"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                publicKey += line;
            }

            return publicKey;

        } catch (Exception e) {
            System.out.println("PublicKeyController: Can't read public key! Error message is " + e.getMessage());

            return null;
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getPublicKey() {
        return new ResponseEntity<>(this.readPublicKey(), HttpStatus.OK);
    }
}
