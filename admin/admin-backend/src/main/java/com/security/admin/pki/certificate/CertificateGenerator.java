package com.security.admin.pki.certificate;

import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;

public class CertificateGenerator {
	public CertificateGenerator() {
	}

	public static X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData,
			List<Integer> keyUsages, String algorithm) {
		try {
			// Posto klasa za generisanje sertifiakta ne moze da primi direktno privatni
			// kljuc pravi se builder za objekat
			// Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za
			// potpisivanje sertifikata
			// Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje
			// sertifiakta
			
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder(algorithm);
			// Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
			builder = builder.setProvider("BC");

			// Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za
			// potpisivanje sertifikata
			ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

			// Postavljaju se podaci za generisanje sertifiakta
			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
					new BigInteger(subjectData.getSerialNumber()), subjectData.getStartDate(), subjectData.getEndDate(),
					subjectData.getX500name(), subjectData.getPublicKey());
			
			if (keyUsages.size() != 0) {
				int keyUsageData = 0;
				for (int keyUsage : keyUsages) {
					keyUsageData = keyUsageData | keyUsage;
				}
				KeyUsage certKeyUsage = new KeyUsage(keyUsageData);
				certGen.addExtension(Extension.keyUsage, true, certKeyUsage);
			}

			// Generise se sertifikat
			X509CertificateHolder certHolder = certGen.build(contentSigner);

			// Builder generise sertifikat kao objekat klase X509CertificateHolder
			// Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se
			// koristi certConverter
			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");

			// Konvertuje objekat u sertifikat
			return certConverter.getCertificate(certHolder);
		} catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException
				| CertIOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
