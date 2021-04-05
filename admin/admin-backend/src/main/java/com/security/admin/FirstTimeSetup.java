package com.security.admin;

import java.io.File;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.security.admin.pki.certificate.CertificateGenerator;
import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;
import com.security.admin.pki.keystore.KeyStoreManager;
import com.security.admin.pki.util.KeyIssuerSubjectGenerator;

@Component
public class FirstTimeSetup {
	private static String keyStorePath;
	
	private static KeyStoreManager keyStoreManager;
	
	@Autowired
	public FirstTimeSetup(@Value("${server.ssl.key-store}") String keyStorePath, KeyStoreManager ksm) {
		FirstTimeSetup.keyStorePath = keyStorePath;
		FirstTimeSetup.keyStoreManager = ksm;
	}

	public static void execute() {
		File f = new File(keyStorePath);
		if (!f.exists()) {
			keyStoreManager.createKeyStore();
			keyStoreManager.saveKeyStore();
			return;
		}
		
		keyStoreManager.loadKeyStore();

		KeyPair kp = KeyIssuerSubjectGenerator.generateKeyPair();
		
		SubjectData subjectData = KeyIssuerSubjectGenerator.generateSubjectData("LotusClinic", "Lotus Clinic Organization",
				"Cyber Security Administrative Center", "Serbia", "Locality", "RS", "lotusclinic505@gmail.com", 1620079200000L, 1651615200000L);

		IssuerData issuerData = KeyIssuerSubjectGenerator.generateIssuerData(kp.getPrivate(), "Mario", "Kujundzic");

		Certificate cert = CertificateGenerator.generateCertificate(subjectData, issuerData);
		
		keyStoreManager.write("sslCertificate", kp.getPrivate(), cert);
		keyStoreManager.saveKeyStore();
	}
}
