package com.security.hospital.pki.util;

import com.security.hospital.pki.data.IssuerData;
import com.security.hospital.pki.data.SubjectData;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KeyIssuerSubjectGenerator {

	public static IssuerData generateIssuerData(PrivateKey issuerKey, String name, String lastName) {
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, name + " " + lastName);
		builder.addRDN(BCStyle.SURNAME, name);
		builder.addRDN(BCStyle.GIVENNAME, lastName);
		builder.addRDN(BCStyle.O, "Cyber Security Hospital Center");
		builder.addRDN(BCStyle.OU, "Cyber Security Administrative Center");
		builder.addRDN(BCStyle.C, "RS");
		builder.addRDN(BCStyle.E, "lotusclinic505@gmail.com");

		// TODO mozda promeniti na nesto drugo
		builder.addRDN(BCStyle.UID, "349678");

		return new IssuerData(issuerKey, builder.build());
	}

	public static SubjectData generateSubjectData(String name, String lastName, String hospitalName,
			String hospitalMail, String startDateString, String endDateString) {
		try {
			KeyPair keyPairSubject = generateKeyPair();

			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse(startDateString);
			Date endDate = iso8601Formater.parse(endDateString);

			// TODO proveriti sta sa ovim
			String sn = "1";

			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
			builder.addRDN(BCStyle.CN, name + " " + lastName);
			builder.addRDN(BCStyle.SURNAME, name);
			builder.addRDN(BCStyle.GIVENNAME, lastName);
			builder.addRDN(BCStyle.O, "Cyber Security Hospital Center");
			builder.addRDN(BCStyle.OU, hospitalName);
			builder.addRDN(BCStyle.C, "RS");
			builder.addRDN(BCStyle.E, hospitalMail);

			// TODO ovo drugacije
			builder.addRDN(BCStyle.UID, "457628");

			return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		return null;
	}
}
