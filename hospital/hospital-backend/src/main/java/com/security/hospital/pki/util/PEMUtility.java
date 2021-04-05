package com.security.hospital.pki.util;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class PEMUtility {
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
        return "-----BEGIN RSA PRIVATE KEY-----\n" + content + "-----END RSA PRIVATE KEY-----";
    }

    public static String publicKeyToPEM(PublicKey publicKey) {
        String content = byteArrayToPEM(publicKey.getEncoded());
        return "-----BEGIN RSA PUBLIC KEY-----\n" + content + "-----END RSA PUBLIC KEY-----";
    }
}
