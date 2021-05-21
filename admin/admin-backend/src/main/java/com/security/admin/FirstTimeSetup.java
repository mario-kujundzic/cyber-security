package com.security.admin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.security.admin.dto.CertificateDTO;
import com.security.admin.pki.certificate.CertificateGenerator;
import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;
import com.security.admin.pki.keystore.KeyStoreManager;
import com.security.admin.pki.util.KeyIssuerSubjectGenerator;
import com.security.admin.pki.util.PEMUtility;
import com.security.admin.service.CertificateService;

@Component
public class FirstTimeSetup {
	private static String keyStoreFolderPath;
	
	private static String keyStorePath;

	private static KeyStoreManager keyStoreManager;

	private static CertificateService certificateService;

	@Autowired
	public FirstTimeSetup(@Value("${server.ssl.key-store}") String keyStorePath, KeyStoreManager ksm,
			CertificateService certService, @Value("${server.ssl.key-store-folder}") String keyStoreFolderPath) {
		FirstTimeSetup.keyStorePath = keyStorePath;
		FirstTimeSetup.keyStoreFolderPath = keyStoreFolderPath;
		FirstTimeSetup.keyStoreManager = ksm;
		FirstTimeSetup.certificateService = certService;
	}

	public static void execute() {
		File f = new File(keyStorePath);

		if (f.exists()) {
			keyStoreManager.loadKeyStore();
			return;
		}

		keyStoreManager.createKeyStore();

		KeyPair kp = KeyIssuerSubjectGenerator.generateKeyPair();

		IssuerData issuerData = KeyIssuerSubjectGenerator.generateIssuerData(kp.getPrivate(), "rootCA");

		SubjectData subjectData = KeyIssuerSubjectGenerator.generateSubjectData(kp.getPublic(), "139095100165847",
				"LotusClinic", "Lotus Clinic Organization", "Cyber Security Administrative Center", "Serbia",
				"Locality", "RS", "lotusclinic505@gmail.com", 1620079200000L, 1651615200000L);

		ArrayList<Integer> purposes = new ArrayList<>();
		purposes.add(128);
		purposes.add(4);
		purposes.add(2);

		Certificate cert = CertificateGenerator.generateCertificate(subjectData, issuerData, purposes,
				"SHA256WithRSAEncryption");

		CertificateDTO dto = new CertificateDTO("LotusClinic", "Lotus Clinic Organization",
				"Cyber Security Administrative Center", "Serbia", "Locality", "RS", "lotusclinic505@gmail.com",
				1620079200000L, 1651615200000L, purposes, "SHA256WithRSAEncryption", 0L);

		certificateService.createCertificateModel(dto, "139095100165847", true);

		keyStoreManager.write("sslCertificate", kp.getPrivate(), cert);
		keyStoreManager.saveKeyStore();

		File privateKeyFile = new File(keyStoreFolderPath + "/key.priv");
		File publicKeyFile = new File(keyStoreFolderPath + "/key.pub");

		String privateKeyPEM = PEMUtility.privateKeyToPEM(kp.getPrivate());
		String publicKeyPEM = PEMUtility.publicKeyToPEM(kp.getPublic());
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(privateKeyFile));
			bufferedWriter.write(privateKeyPEM);
			bufferedWriter.close();
			System.out.println("FirstTimeSetup: Private key saved to file.");
	
			bufferedWriter = new BufferedWriter(new FileWriter(publicKeyFile));
			bufferedWriter.write(publicKeyPEM);
			bufferedWriter.close();
			System.out.println("FirstTimeSetup: Public key saved to file.");
			
			PEMUtility.writeCertToPEM(cert, keyStoreFolderPath + "/rootCA.crt");
		}
		catch (Exception e) {
			System.out.println("Failed saving keys and cert to file");
		}
	}
}
