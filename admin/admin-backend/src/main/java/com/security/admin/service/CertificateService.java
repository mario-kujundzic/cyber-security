package com.security.admin.service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.admin.dto.CertificateDTO;
import com.security.admin.model.CertificateSigningRequest;
import com.security.admin.model.CertificateSigningRequestStatus;
import com.security.admin.pki.certificate.CertificateGenerator;
import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;
import com.security.admin.pki.keystore.KeyStoreManager;
import com.security.admin.pki.util.Base64Utility;
import com.security.admin.pki.util.KeyIssuerSubjectGenerator;
import com.security.admin.repository.CertificateRepository;

@Service
public class CertificateService {

	@Autowired
	private KeyStoreManager keyStoreManager;

	@Autowired
	private CertificateRepository certificateRepository;

	@Autowired
	private CertificateSigningRequestService certRequestService;

	public List<CertificateDTO> getAll() {
		List<CertificateDTO> list = new ArrayList<>();
		keyStoreManager.readAllCertificates().forEach(c -> {
			try {
				list.add(toDTO(c));
			} catch (CertificateEncodingException e) {
				e.printStackTrace();
			}
		});
		return list;
	}

	private CertificateDTO toDTO(Certificate c) throws CertificateEncodingException {
		CertificateDTO dto = new CertificateDTO();
		JcaX509CertificateHolder holder = new JcaX509CertificateHolder((X509Certificate) c);

		X500Name subject = holder.getSubject();
		dto.setCommonName(subject.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString());
		dto.setOrganization(subject.getRDNs(BCStyle.O)[0].getFirst().getValue().toString());
		dto.setOrganizationUnit(subject.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString());
		dto.setLocality(subject.getRDNs(BCStyle.L)[0].getFirst().getValue().toString());
		dto.setState(subject.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString());
		dto.setCountry(subject.getRDNs(BCStyle.C)[0].getFirst().getValue().toString());
		dto.setEmail(subject.getRDNs(BCStyle.EmailAddress)[0].getFirst().getValue().toString());

		dto.setSerialNumber(holder.getSerialNumber());
		dto.setValidFrom(holder.getNotBefore().getTime());
		dto.setValidTo(holder.getNotAfter().getTime());

		// TODO: jako fishy, videti sta sa ovim
		if (holder.getSignatureAlgorithm().getAlgorithm().toString().equals("1.2.840.113549.1.1.11"))
			dto.setAlgorithm("SHA 256 with RSA");
		else
			dto.setAlgorithm("SHA 512 with RSA");

		// TODO: razmisliti o purpose i Extensions
		// dto.setPurposeReadable(getCertificatePurpose(holder.getExtensions());
		// kako na osnovu necega kao "1.2.840.113549.1.1.11" doci do necega kao sto je
		// Digital signature
		List<String> purposes = new ArrayList<>();
		purposes.add("Digital signature"); // za sad zapucano
		dto.setPurposeReadable(purposes);

		X500Name issuer = holder.getIssuer();
		dto.setIssuer(issuer.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString());

		return dto;
	}

	public CertificateDTO createCertificate(CertificateDTO dto) {
		try {
			// TODO IMPORTANT: promeniti ove kljuceve da budu - privatni od servera, public
			// od onog ko je requestovao
			CertificateSigningRequest req = certRequestService.getOne(dto.getRequestId());

			byte[] publicBytes = Base64Utility.decode(req.getPublicKey());
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(keySpec);

			KeyPair kp = KeyIssuerSubjectGenerator.generateKeyPair();

			SubjectData subjectData = KeyIssuerSubjectGenerator.generateSubjectData(pubKey, dto.getCommonName(),
					dto.getOrganization(), dto.getOrganizationUnit(), dto.getLocality(), dto.getState(),
					dto.getCountry(), dto.getEmail(), dto.getValidFrom(), dto.getValidTo());

			// za sada samo jedan issuer
			IssuerData issuerData = KeyIssuerSubjectGenerator.generateIssuerData(kp.getPrivate(), "rootCA");

			Certificate cert = CertificateGenerator.generateCertificate(subjectData, issuerData, dto.getPurpose(),
					dto.getAlgorithm());

			// da li je ok da alias bude serial number?
			keyStoreManager.write(subjectData.getSerialNumber(), kp.getPrivate(), cert);
			keyStoreManager.saveKeyStore();
			req.setStatus(CertificateSigningRequestStatus.SIGNED);
			return toDTO(cert);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public CertificateDTO revokeCertifikate(String serialNumber) {
		BigInteger sn = new BigInteger(serialNumber);
		com.security.admin.model.Certificate crt = certificateRepository.findOneBySerialNumber(sn);
		if (crt == null)
			return null;
		crt.setRevocationStatus(true);
		certificateRepository.save(crt);

		// obrisi iz keystora
		CertificateDTO revokedCert = null;

		try {
			Certificate certificate = keyStoreManager.removeCertificate(serialNumber);
			revokedCert = toDTO(certificate);
			keyStoreManager.saveKeyStore();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return revokedCert;
	}

	// katastrofa
	private List<String> getCertificatePurpose(List<Integer> purposes) {
		List<String> purposeHumanReadable = new ArrayList<>();
		for (Integer i : purposes) {
			if (i == 128) {
				purposeHumanReadable.add("Digital signature");
			} else if (i == 2) {
				purposeHumanReadable.add("CRL sign");
			} else if (i == 16) {
				purposeHumanReadable.add("Data encipherment");
			} else if (i == 32768) {
				purposeHumanReadable.add("Decipher only");
			} else if (i == 1) {
				purposeHumanReadable.add("Encipher only");
			} else if (i == 8) {
				purposeHumanReadable.add("Key agreement");
			} else if (i == 4) {
				purposeHumanReadable.add("Key certificate sign");
			} else if (i == 32) {
				purposeHumanReadable.add("Key encipherment");
			} else if (i == 64) {
				purposeHumanReadable.add("Non repudiation");
			}
		}
		return purposeHumanReadable;
	}
}
