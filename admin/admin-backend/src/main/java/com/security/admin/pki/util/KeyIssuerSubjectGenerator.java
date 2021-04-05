package com.security.admin.pki.util;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;

public class KeyIssuerSubjectGenerator {

	public static IssuerData generateIssuerData(PrivateKey issuerKey, String name, String lastName) {
		// za sada imamo samo jednog issuera
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, name + " " + lastName);
		builder.addRDN(BCStyle.SURNAME, name);
		builder.addRDN(BCStyle.GIVENNAME, lastName);
		builder.addRDN(BCStyle.O, "Cyber Security Hospital Center");
		builder.addRDN(BCStyle.OU, "Cyber Security Administrative Center");
		builder.addRDN(BCStyle.C, "RS");
		builder.addRDN(BCStyle.E, "lotusclinic505@gmail.com");

		builder.addRDN(BCStyle.UID, RandomUtil.getRandomBigInteger().toString());

		return new IssuerData(issuerKey, builder.build());
	}

	public static SubjectData generateSubjectData(String commonName, String organization, String organizationUnit,
			String locality, String state, String country, String email, long startDate, long endDate) {

		KeyPair keyPairSubject = generateKeyPair();
		// TODO proveriti sta sa ovim
		String sn = RandomUtil.getRandomBigInteger().toString();

		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, commonName);
		builder.addRDN(BCStyle.O, organization);
		builder.addRDN(BCStyle.OU, organizationUnit);
		builder.addRDN(BCStyle.L, locality);
		builder.addRDN(BCStyle.ST, state);
		builder.addRDN(BCStyle.C, country);
		builder.addRDN(BCStyle.EmailAddress, email);

		builder.addRDN(BCStyle.UID, sn);
		return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, new Date(startDate), new Date(endDate));

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
