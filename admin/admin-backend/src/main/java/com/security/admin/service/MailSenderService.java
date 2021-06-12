package com.security.admin.service;

import java.util.concurrent.Future;

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
	

}
