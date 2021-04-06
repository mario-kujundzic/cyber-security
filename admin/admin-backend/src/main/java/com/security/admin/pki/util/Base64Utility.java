package com.security.admin.pki.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

public class Base64Utility {
    // Pomocna funkcija za enkodovanje bajtova u string
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    // Pomocna funkcija za dekodovanje stringa u bajt niz
    public static byte[] decode(String base64Data) throws IOException {
        return Base64.getDecoder().decode(base64Data);
    }
    
    public static PublicKey decodePublicKeyPEM(String base64PEMString) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    	String cleanString = base64PEMString.split("-----BEGIN RSA PUBLIC KEY-----")[1].split("-----END RSA PUBLIC KEY-----")[0];
    	cleanString = cleanString.replace("\r\n", "");
		byte[] publicBytes = decode(cleanString);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		return pubKey;
    }
    
    public static void writeCertToFilePEM(Certificate cert, String name) throws IOException, CertificateEncodingException {
    	//FileOutputStream out = new FileOutputStream(new File("./name"));
    	FileWriter fileWriter = new FileWriter(new File("./cert_id_" + name + ".crt"));
    	try (JcaPEMWriter pemWriter = new JcaPEMWriter(fileWriter)) {
    		pemWriter.writeObject(cert);
    		pemWriter.flush();
    	}
    }
}
