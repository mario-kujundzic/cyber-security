package com.security.admin.pki.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyPairUtility {

    public static PrivateKey readPrivateKey(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String PEMString = new String(bytes);

        return PEMUtility.PEMToPrivateKey(PEMString);
    }

    public static PublicKey readPublicKey(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String PEMString = new String(bytes);

        return PEMUtility.PEMToPublicKey(PEMString);
    }

    public static String readPEM(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }

}
