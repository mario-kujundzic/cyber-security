package com.security.admin;

import java.security.Security;
import java.util.regex.Pattern;

import com.security.admin.util.ValidationUtility;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(AdminApplication.class, args);
		FirstTimeSetup.execute();
	}

}
