package com.security.admin.service;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.admin.dto.CertificateDTO;
import com.security.admin.pki.certificate.CertificateGenerator;
import com.security.admin.pki.data.IssuerData;
import com.security.admin.pki.data.SubjectData;
import com.security.admin.pki.keystore.KeyStoreManager;
import com.security.admin.pki.util.KeyIssuerSubjectGenerator;

@Service
public class CertificateService {

	@Autowired
	private KeyStoreManager keyStoreManager;
	
	public List<CertificateDTO> getAll() {
		List<CertificateDTO> list = new ArrayList<>();
		keyStoreManager.readAllCertificates().forEach(c -> {
			try {
				list.add(toDTO(c));
			} catch (CertificateEncodingException e) {
				// TODO Auto-generated catch block
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
		
		// TODO: proveriti da li je ova linija za algoritam okej
		dto.setAlgorithm(holder.getSignatureAlgorithm().toString());
		
		// TODO: razmisliti o purpose i Extensions
		
		//X500Name issuer = holder.getIssuer();
		
        return dto;
	}

	public CertificateDTO createCertificate(CertificateDTO dto) {
		try {
			// TODO IMPORTANT: promeniti ove kljuceve da budu - privatni od servera, public od onog ko je requestovao
			KeyPair kp = KeyIssuerSubjectGenerator.generateKeyPair();
						
			SubjectData subjectData = KeyIssuerSubjectGenerator.generateSubjectData(dto.getCommonName(), dto.getOrganization(), dto.getOrganizationUnit(), 
					dto.getLocality(), dto.getState(), dto.getCountry(), dto.getEmail(), dto.getValidFrom(), dto.getValidTo());
			
			// za sada samo jedan issuer
			IssuerData issuerData = KeyIssuerSubjectGenerator.generateIssuerData(kp.getPrivate(), "Mario", "Kujundzic");
			
			Certificate cert = CertificateGenerator.generateCertificate(subjectData, issuerData, dto.getPurpose(), dto.getAlgorithm());
			
			// da li je ok da alias bude serial number?
			keyStoreManager.write(subjectData.getSerialNumber(), kp.getPrivate(), cert);
			keyStoreManager.saveKeyStore();
			return toDTO(cert);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
