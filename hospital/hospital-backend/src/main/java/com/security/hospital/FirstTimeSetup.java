package com.security.hospital;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.security.hospital.pki.keystore.KeyStoreManager;
import com.security.hospital.pki.keystore.TrustStoreManager;
import com.security.hospital.pki.util.KeyIssuerSubjectGenerator;
import com.security.hospital.pki.util.PEMUtility;

@Component
public class FirstTimeSetup {
	private static String keyStoreFolderPath;

	private static KeyStoreManager keyStoreManager;

	private static TrustStoreManager trustStoreManager;

	@Autowired
	public FirstTimeSetup(@Value("${server.ssl.key-store-folder}") String keyStoreFolderPath, KeyStoreManager ksm,
			TrustStoreManager tsm) {
		FirstTimeSetup.keyStoreFolderPath = keyStoreFolderPath;
		FirstTimeSetup.keyStoreManager = ksm;
		FirstTimeSetup.trustStoreManager = tsm;
	}

	public static void execute() throws IOException {
		File keyStoreFile = new File(keyStoreFolderPath + "/keystore.p12");
		File privateKeyFile = new File(keyStoreFolderPath + "/key.priv");
		File publicKeyFile = new File(keyStoreFolderPath + "/key.pub");
		File certFile = new File(keyStoreFolderPath + "/hospitalBack.crt");
		
		if (keyStoreFile.exists() && privateKeyFile.exists() && publicKeyFile.exists()) {
			System.out.println("FirstTimeSetup: KeyStore, private key and public key found at specified path.");
			keyStoreManager.loadKeyStore();
			if (certFile.exists()) {
				if (keyStoreManager.certExists()) {
					System.out.println("Certificate file found, KeyStore already has cert: ignoring...");
				} else {
					System.out.println("Certificate file found, KeyStore empty: loading into KeyStore...");
					keyStoreManager.loadCertificate(certFile, privateKeyFile);
					keyStoreManager.saveKeyStore();
				}
			}
			return;
		}

		// TODO: dodati neki generisani cert da bi mogao prvi put da posalje zahtev za
		// novim cert-om

		keyStoreManager.createKeyStore();
		System.out.println("FirstTimeSetup: KeyStore created.");
		trustStoreManager.createKeyStore();
		trustStoreManager.saveKeyStore();

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
