package com.security.hospital.pki.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PEMUtility {

    public static final String privateKeyPrefix = "-----BEGIN RSA PRIVATE KEY-----";
    public static final String privateKeySuffix = "-----END RSA PRIVATE KEY-----";

    public static final String publicKeyPrefix = "-----BEGIN RSA PUBLIC KEY-----";
    public static final String publicKeySuffix = "-----END RSA PUBLIC KEY-----";

    private static String byteArrayToPEM(byte[] input) {
        String base64Encoded = Base64.getEncoder().encodeToString(input);

        StringBuilder rowStringBuilder = new StringBuilder();
        StringBuilder formattedKeyStringBuilder = new StringBuilder();

        for (int i = 0; i < base64Encoded.length(); i++) {
            rowStringBuilder.append(base64Encoded.charAt(i));
            if ((i+1) % 64 == 0) {
                formattedKeyStringBuilder.append(rowStringBuilder.toString() + System.lineSeparator());
                rowStringBuilder = new StringBuilder();
            }
        }
        formattedKeyStringBuilder.append(rowStringBuilder + System.lineSeparator());

        return formattedKeyStringBuilder.toString();
    }

    public static String privateKeyToPEM(PrivateKey privateKey) {
        String content = byteArrayToPEM(privateKey.getEncoded());
        return privateKeyPrefix + content + privateKeySuffix;
    }

    public static String publicKeyToPEM(PublicKey publicKey) {
        String content = byteArrayToPEM(publicKey.getEncoded());
        return publicKeyPrefix + content + publicKeySuffix;
    }

    public static PrivateKey PEMToPrivateKey(String PEM) {
        String base64Content = PEM.substring(privateKeyPrefix.length(), PEM.length() - privateKeySuffix.length());
        base64Content = base64Content.replace("\n", "");
        base64Content = base64Content.replace("\r", "");
        byte[] byteContent = Base64.getDecoder().decode(base64Content);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(byteContent));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PublicKey PEMToPublicKey(String PEM) {
        String base64Content = PEM.substring(publicKeyPrefix.length(), PEM.length() - publicKeySuffix.length());
        base64Content = base64Content.replace("\n", "");
        base64Content = base64Content.replace("\r", "");
        byte[] byteContent = Base64.getDecoder().decode(base64Content);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(byteContent));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }
}
