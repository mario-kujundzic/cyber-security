package com.security.hospital.pki.keystore;

import com.security.hospital.pki.data.IssuerData;
import com.security.hospital.pki.util.PEMUtility;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeyStoreManager {

	// KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste
	// za cuvanje kljuceva
	// Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
	// - Sertifikati koji ukljucuju javni kljuc
	// - Privatni kljucevi
	// - Tajni kljucevi, koji se koriste u simetricnima siframa

	private KeyStore keyStore;

	private String fileName;

	private char[] password;

	@Autowired
	public KeyStoreManager(@Value("${server.ssl.key-store-password}") String keyStorePassword,
			@Value("${server.ssl.key-store-folder}") String keyStoreFolderPath) {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
			this.fileName = keyStoreFolderPath + "/keystore.p12";
			this.password = keyStorePassword.toCharArray();
		} catch (KeyStoreException | NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	public void createKeyStore() {
		try {
			keyStore.load(null, this.password);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
	}

	public void loadKeyStore() {
		try {
			keyStore.load(new FileInputStream(this.fileName), this.password);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
	}

	public void saveKeyStore() {
		try {
			keyStore.store(new FileOutputStream(this.fileName), this.password);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String alias, PrivateKey privateKey, Certificate certificate) {
		try {
			keyStore.setKeyEntry(alias, privateKey, this.password, new Certificate[] { certificate });
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	public IssuerData readIssuerFromStore(String alias, char[] keyPass) {
		try {
			Certificate cert = keyStore.getCertificate(alias);

			PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
			return new IssuerData(privKey, issuerName);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Certificate readCertificate(String alias) {
		try {
			if (keyStore.isKeyEntry(alias)) {
				Certificate cert = keyStore.getCertificate(alias);
				return cert;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PrivateKey readPrivateKey(String alias, String pass) {
		try {
			if (keyStore.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) keyStore.getKey(alias, pass.toCharArray());
				return pk;
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean certExists() {
		try {
			return keyStore.isKeyEntry("sslCertificate");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public List<Certificate> readAllCertificates() {
		List<Certificate> list = new ArrayList<>();
		try {
			Enumeration<String> aliases = keyStore.aliases();
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				Certificate cert = this.readCertificate(alias);
				list.add(cert);
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void savePrivateKeyToFile(PrivateKey privateKey) {

	}

	public void loadCertificate(File certFile, File privateKeyFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(privateKeyFile));
			String privateKeyString = reader.lines().collect(Collectors.joining());
			PrivateKey privateKey = PEMUtility.PEMToPrivateKey(privateKeyString);
			reader.close();
			FileInputStream certStream = new FileInputStream(certFile);
			Certificate cert = PEMUtility.PEMToCertificate(certStream);

			keyStore.setKeyEntry("sslCertificate", privateKey, this.password, new Certificate[] { cert });
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
