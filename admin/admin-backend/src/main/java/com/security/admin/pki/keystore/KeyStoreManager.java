package com.security.admin.pki.keystore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.security.admin.pki.data.IssuerData;

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
			@Value("${server.ssl.key-store}") String keyStorePath) {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
			this.fileName = keyStorePath;
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

	public void write(String alias, PrivateKey privateKey, Certificate[] certificateChain) {
		try {
			keyStore.setKeyEntry(alias, privateKey, this.password, certificateChain);
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

	public PrivateKey readPrivateKey(String alias) {
		try {
			if (keyStore.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) keyStore.getKey(alias, this.password);
				return pk;
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
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
	
	public Certificate removeCertificate(String serialNumber) throws Exception {
		Certificate cert = this.readCertificate(serialNumber);
		keyStore.deleteEntry(serialNumber);
		return cert;
	}

	public KeyStore getKeyStore() {
		return keyStore;
	}
}
