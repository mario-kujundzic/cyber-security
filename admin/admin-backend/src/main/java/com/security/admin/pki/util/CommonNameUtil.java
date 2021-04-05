package com.security.admin.pki.util;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

public class CommonNameUtil {
	
	public static String getCommonName(X509Certificate certificate) throws CertificateEncodingException {
        X500Name x500name = new JcaX509CertificateHolder(certificate).getSubject();
        RDN cn = x500name.getRDNs(BCStyle.CN)[0];

        return cn.getFirst().getValue().toString();
    }

    public static String getIssuerCommonName(X509Certificate certificate) {
        String issuerData = certificate.getIssuerX500Principal().getName();
        String[] parts = issuerData.split("CN=");
        int index = parts[1].indexOf(',') == -1 ? parts[1].length() : parts[1].indexOf(',');

        return parts[1].substring(0, index);
    }

}
