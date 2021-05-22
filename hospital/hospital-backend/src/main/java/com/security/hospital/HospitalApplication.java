package com.security.hospital;

import com.security.hospital.pki.util.KeyIssuerSubjectGenerator;
import com.security.hospital.pki.util.PEMUtility;
import com.security.hospital.util.ValidationUtility;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.Security;
import java.util.Base64;
import java.util.regex.Pattern;

@SpringBootApplication
public class HospitalApplication {

	public static void main(String[] args) throws IOException {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(HospitalApplication.class, args);
		FirstTimeSetup.execute();
	}

}
