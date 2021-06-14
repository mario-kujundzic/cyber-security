package com.security.admin.service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;


@Component
public class MailSenderService {
	
	private JavaMailSender emailSender;

    @Value("${registration.link}")
    private String registrationLink;
    
    @Value("${recovery.link}")
    private String recoveryLink;
    
    @Autowired
    public MailSenderService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public Future<SimpleMailMessage> sendEmail(String recipient, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
        return new AsyncResult<>(message);
    }
    
    @Async
    public Future<SimpleMailMessage> forgotPassword(String username, String key) {
    	String content = "Hi " + username + "!" + "\n\nSorry to hear you're having trouble logging into CyberHeaven application." + 
    			"\n\nWe can help you get straight back into your account.\n\n" +
    			"Please follow this link to reset your password:\n" + recoveryLink + "/" + key;
    	return sendEmail(username, username + ", we've made it easy to get back on CyberHeaven application", content);
    			
    }
    
    @Async
    public void sendCertificate(String commonName, String email, String certPath) throws AddressException, MessagingException, IOException {
    	String content = "Cao, " + commonName + "!" + "\n\nU prilogu ti se nalazi sertifikat koji si trazio." +
    					"\n\nU zdravlju da ga koristis!" +
    					"\n\nPuno te pozdravlja i voli tvoj super admin!";
    	
    	String subject = "Knock, knock - your certificate is here!";
    	
    	MimeMessage mimeMessage = emailSender.createMimeMessage();
    	mimeMessage.setSender(new InternetAddress("lotusclinic505@gmail.com"));
    	mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
    	mimeMessage.setSubject(subject);
    	    	
    	MimeBodyPart mimeBodyPart = new MimeBodyPart();
    	mimeBodyPart.setContent(content, "text/html");
    	
    	Multipart multipart = new MimeMultipart();
    	multipart.addBodyPart(mimeBodyPart);
    	
    	MimeBodyPart attachmentBodyPart = new MimeBodyPart();
    	attachmentBodyPart.attachFile(new File(certPath));
    	
    	multipart.addBodyPart(attachmentBodyPart);
    	
    	mimeMessage.setContent(multipart);
    	
    	emailSender.send(mimeMessage);
    	
    }
	

}
