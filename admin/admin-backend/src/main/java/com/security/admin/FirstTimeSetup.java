package com.security.admin;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.security.admin.pki.certificate.CertificateGenerator;
import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;
import com.security.admin.pki.keystore.KeyStoreWriter;
import com.security.admin.pki.util.KeyIssuerSubjectGenerator;

@Component
public class FirstTimeSetup {
	private static String keyStorePassword;
	private static String keyStorePath;

	@Autowired
	public FirstTimeSetup(@Value("${server.ssl.key-store-password}") String keyStorePassword,
			@Value("${server.ssl.key-store}") String keyStorePath) {
		FirstTimeSetup.keyStorePassword = keyStorePassword;
		FirstTimeSetup.keyStorePath = keyStorePath;
	}

	public static void execute() {
		File f = new File(keyStorePath);
		KeyStoreWriter ksw = new KeyStoreWriter();
		if (f.exists()) {
			ksw.loadKeyStore(FirstTimeSetup.keyStorePath, FirstTimeSetup.keyStorePassword.toCharArray());

		} else {
			ksw.loadKeyStore(null, FirstTimeSetup.keyStorePassword.toCharArray());

			KeyPair kp = KeyIssuerSubjectGenerator.generateKeyPair();

			SubjectData subjectData = KeyIssuerSubjectGenerator.generateSubjectData("Mario", "Kujundzic",
					"Cyber Security Administrative Center", "lotusclinic505@gmail.com", "2021-01-01", "2023-01-01");

			IssuerData issuerData = KeyIssuerSubjectGenerator.generateIssuerData(kp.getPrivate(), "Mario", "Kujundzic");

			Certificate cert = CertificateGenerator.generateCertificate(subjectData, issuerData);
			
			ksw.write("sslCertificate", kp.getPrivate(), keyStorePassword.toCharArray(), cert);
			ksw.saveKeyStore(FirstTimeSetup.keyStorePath, FirstTimeSetup.keyStorePassword.toCharArray());
		}

	}
}
