package com.security.hospital;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.Security;

@SpringBootApplication
public class HospitalApplication {

	public static void main(String[] args) throws IOException {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(HospitalApplication.class, args);
		FirstTimeSetup.execute();
	}

}
