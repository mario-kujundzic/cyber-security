package com.security.hospital;

import com.security.hospital.pki.certificate.CertificateGenerator;
import com.security.hospital.pki.data.IssuerData;
import com.security.hospital.pki.data.SubjectData;
import com.security.hospital.pki.keystore.KeyStoreManager;
import com.security.hospital.pki.util.KeyIssuerSubjectGenerator;
import com.security.hospital.pki.util.PEMUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.util.Base64;

@Component
public class FirstTimeSetup {
	private static String keyStoreFolderPath;
	
	private static KeyStoreManager keyStoreManager;
	
	@Autowired
	public FirstTimeSetup(@Value("${server.ssl.key-store-folder}") String keyStoreFolderPath, KeyStoreManager ksm) {
		FirstTimeSetup.keyStoreFolderPath = keyStoreFolderPath;
		FirstTimeSetup.keyStoreManager = ksm;
	}

	public static void execute() throws IOException {
		File keyStoreFile = new File(keyStoreFolderPath + "/keystore.p12");
		File privateKeyFile = new File(keyStoreFolderPath + "/key.priv");
		File publicKeyFile = new File(keyStoreFolderPath + "/key.pub");

		if (keyStoreFile.exists() && privateKeyFile.exists() && publicKeyFile.exists()) {
			System.out.println("FirstTimeSetup: KeyStore, private key and public key found at specified path.");
			keyStoreManager.loadKeyStore();
			return;
		}

		keyStoreManager.createKeyStore();
		System.out.println("FirstTimeSetup: KeyStore created.");

		KeyPair kp = KeyIssuerSubjectGenerator.generateKeyPair();

		String privateKeyPEM = PEMUtility.privateKeyToPEM(kp.getPrivate());
		String publicKeyPEM = PEMUtility.publicKeyToPEM(kp.getPublic());

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(privateKeyFile));
		bufferedWriter.write(privateKeyPEM);
		bufferedWriter.close();
		System.out.println("FirstTimeSetup: Private key saved to file.");

		bufferedWriter = new BufferedWriter(new FileWriter(publicKeyFile));
		bufferedWriter.write(publicKeyPEM);
		bufferedWriter.close();
		System.out.println("FirstTimeSetup: Public key saved to file.");

		keyStoreManager.saveKeyStore();
	}
}
