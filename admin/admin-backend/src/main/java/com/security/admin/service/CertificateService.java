package com.security.admin.service;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.security.admin.dto.CertificateDTO;
import com.security.admin.dto.CertificateStatusDTO;
import com.security.admin.enums.CertificateStatus;
import com.security.admin.model.requests.CertificateSigningRequest;
import com.security.admin.model.requests.RequestStatus;
import com.security.admin.pki.certificate.CertificateGenerator;
import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;
import com.security.admin.pki.keystore.CrlKeyStoreManager;
import com.security.admin.pki.keystore.KeyStoreManager;
import com.security.admin.pki.keystore.TrustStoreManager;
import com.security.admin.pki.util.CryptographicUtility;
import com.security.admin.pki.util.KeyIssuerSubjectGenerator;
import com.security.admin.pki.util.KeyPairUtility;
import com.security.admin.pki.util.PEMUtility;
import com.security.admin.pki.util.RandomUtil;
import com.security.admin.repository.CertificateRepository;

@Service
public class CertificateService {

	private KeyStoreManager keyStoreManager;
	
	private CrlKeyStoreManager crlKeyStoreManager;

	private CertificateRepository certificateRepository;

	private CertificateSigningRequestService certRequestService;
	
	private TrustStoreManager trustStoreManager;
	
	private String resourceFolderPath;
	

	@Autowired
	public CertificateService(@Value("${server.ssl.key-store-folder}") String resourceFolderPath, KeyStoreManager keyStoreManager, CertificateRepository certificateRepository,
			CertificateSigningRequestService certRequestService, TrustStoreManager trustStoreManager, CrlKeyStoreManager crlKeyStoreManager) {
		this.keyStoreManager = keyStoreManager;
		this.crlKeyStoreManager = crlKeyStoreManager;
		this.certificateRepository = certificateRepository;
		this.certRequestService = certRequestService;
		this.trustStoreManager = trustStoreManager;
		this.resourceFolderPath = resourceFolderPath;
	}

	public List<CertificateDTO> getAll() {
		List<CertificateDTO> list = new ArrayList<>();
		keyStoreManager.readAllCertificates().forEach(c -> {
			try {
				list.add(toDTO(c));
			} catch (Exception e) {
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

		if (holder.getSignatureAlgorithm().getAlgorithm().toString().equals("1.2.840.113549.1.1.11"))
			dto.setAlgorithm("SHA 256 with RSA");
		else
			dto.setAlgorithm("SHA 512 with RSA");

		Extension ex = holder.getExtension(holder.getExtensions().getExtensionOIDs()[0]);
		ASN1OctetString octString = ex.getExtnValue();
		String octStr = octString.toString().substring(1);
		Long number = Long.parseLong(octStr, 16);
		String binaryString = Long.toBinaryString(number);
		List<String> purposes = getCertificatePurpose(binaryString);
		dto.setPurposeReadable(purposes);

		X500Name issuer = holder.getIssuer();
		dto.setIssuer(issuer.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString());

		com.security.admin.model.Certificate crt = certificateRepository
				.findOneBySerialNumber(holder.getSerialNumber());

		if (crt != null) {
			dto.setRootAuthority(crt.isRootAuthority());
		}

		return dto;
	}

	public CertificateDTO createCertificate(CertificateDTO dto) {
		try {
			// TODO IMPORTANT: promeniti ove kljuceve da budu - privatni od servera, public
			// od onog ko je requestovao
			CertificateSigningRequest req = certRequestService.getOne(dto.getRequestId());

			PublicKey pubKey = PEMUtility.PEMToPublicKey(req.getPublicKey());

			PrivateKey privKey = keyStoreManager.readPrivateKey("sslCertificate");

			String serial = RandomUtil.getRandomBigInteger().toString();

			SubjectData subjectData = KeyIssuerSubjectGenerator.generateSubjectData(pubKey, serial, dto.getCommonName(),
					dto.getOrganization(), dto.getOrganizationUnit(), dto.getLocality(), dto.getState(),
					dto.getCountry(), dto.getEmail(), dto.getValidFrom(), dto.getValidTo());

			// za sada samo jedan issuer
			IssuerData issuerData = KeyIssuerSubjectGenerator.generateIssuerData(privKey, "LotusClinic");

			Certificate cert = CertificateGenerator.generateCertificate(subjectData, issuerData, dto.getPurpose(),
					dto.getAlgorithm());

			Certificate rootCert = keyStoreManager.readCertificate("sslCertificate");

			Certificate[] certChain = { cert, rootCert };
			// da li je ok da alias bude serial number?
			keyStoreManager.write(subjectData.getSerialNumber(), privKey, certChain);
			trustStoreManager.writeCert(subjectData.getSerialNumber(), cert);

			keyStoreManager.saveKeyStore();
			trustStoreManager.saveKeyStore();
			req.setStatus(RequestStatus.SIGNED);

			certRequestService.save(req);

			createCertificateModel(dto, serial, false);

			PEMUtility.writeCertToPEM(certChain, "./cert_" + dto.getCommonName() + ".crt");

			return toDTO(cert);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public CertificateDTO revokeCertificate(String serialNumber, String revocationReason) throws Exception {
		BigInteger sn = new BigInteger(serialNumber);
		com.security.admin.model.Certificate crt = certificateRepository.findOneBySerialNumber(sn);
		if (crt == null)
			throw new Exception();
		if (crt.isRootAuthority())
			throw new Exception();

		crt.setValidTo(new Date());
		crt.setRevocationStatus(true);
		crt.setRevocationReason(revocationReason);
		certificateRepository.save(crt);

		// obrisi iz keystora
		Certificate certificate = keyStoreManager.removeCertificate(serialNumber);
		
		// sacuvaj u crl keystore
		crlKeyStoreManager.write(serialNumber, certificate);
		crlKeyStoreManager.saveKeyStore();
		
		CertificateDTO revokedCert = toDTO(certificate);
		keyStoreManager.saveKeyStore();

		return revokedCert;
	}

	public List<CertificateDTO> getRevokedCerts() {
		List<com.security.admin.model.Certificate> crts = certificateRepository.findAllByRevocationStatus(true);
		List<CertificateDTO> crtsDTO = new ArrayList<>();
		for (com.security.admin.model.Certificate c : crts) {
			CertificateDTO dto = new CertificateDTO();
			dto.setSerialNumber(c.getSerialNumber());
			dto.setRevocationReason(c.getRevocationReason());
			dto.setValidFrom(c.getValidFrom().getTime());
			dto.setValidTo(c.getValidTo().getTime());
			dto.setEmail(c.getEmail());
			dto.setCommonName(c.getCommonName());
			crtsDTO.add(dto);
		}
		return crtsDTO;
	}

	private List<String> getCertificatePurpose(String binaryPurposes) {
		List<String> purposeHumanReadable = new ArrayList<>();
		if (binaryPurposes.charAt(18) == '1') {
			purposeHumanReadable.add("Digital signature");
		}
		if (binaryPurposes.charAt(24) == '1') {
			purposeHumanReadable.add("CRL sign");
		}
		if (binaryPurposes.charAt(21) == '1') {
			purposeHumanReadable.add("Data encipherment");
		}
		if (binaryPurposes.charAt(10) == '1') {
			purposeHumanReadable.add("Decipher only");
		}
		if (binaryPurposes.charAt(25) == '1') {
			purposeHumanReadable.add("Encipher only");
		}
		if (binaryPurposes.charAt(22) == '1') {
			purposeHumanReadable.add("Key agreement");
		}
		if (binaryPurposes.charAt(23) == '1') {
			purposeHumanReadable.add("Key certificate sign");
		}
		if (binaryPurposes.charAt(20) == '1') {
			purposeHumanReadable.add("Key encipherment");
		}
		if (binaryPurposes.charAt(19) == '1') {
			purposeHumanReadable.add("Non repudiation");
		}
		return purposeHumanReadable;
	}

	public void createCertificateModel(CertificateDTO dto, String serial, boolean isRootAuthority) {
		com.security.admin.model.Certificate modelCert = new com.security.admin.model.Certificate();

		modelCert.setCommonName(dto.getCommonName());
		modelCert.setEmail(dto.getEmail());
		modelCert.setRevocationStatus(false);
		modelCert.setRootAuthority(isRootAuthority);
		modelCert.setSerialNumber(new BigInteger(serial));
		modelCert.setValidFrom(new Date(dto.getValidFrom()));
		modelCert.setValidTo(new Date(dto.getValidTo()));

		certificateRepository.save(modelCert);
	}

	public CertificateStatusDTO checkCertificateStatus(String serialNumber) throws Exception {
		Certificate certificate = keyStoreManager.readCertificate(serialNumber);
		CertificateStatusDTO status = new CertificateStatusDTO(serialNumber);
		BigInteger sn = new BigInteger(serialNumber);
		if (certificate != null) {
			com.security.admin.model.Certificate c = certificateRepository.findOneBySerialNumber(sn);
			if (c.isRevocationStatus()) {
				status.setStatus(CertificateStatus.REVOKED);
			} else if (c.getValidTo().before(new Date(Instant.now().getEpochSecond()))) {
				revokeCertificate(serialNumber, "Certificate expired");
				status.setStatus(CertificateStatus.EXPIRED);
			} else {
				status.setStatus(CertificateStatus.ACTIVE);
			}
		} else {
			status.setStatus(CertificateStatus.NOT_EXIST);
		}

		byte[] csrBytes = status.getCSRBytes();
		PrivateKey privateKey = KeyPairUtility.readPrivateKey(resourceFolderPath + "/key.priv");
		byte[] signature = CryptographicUtility.sign(csrBytes, privateKey);
		String base64Signature = Base64.getEncoder().encodeToString(signature);
		status.setSignature(base64Signature); 
		
		return status;
		
	}
}
