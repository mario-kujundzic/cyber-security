package com.security.hospital;

import org.springframework.context.ApplicationContext;

import com.security.hospital.model.drools.Test;
import com.security.hospital.service.KieSessionService;

public class SessionInitializer {
	public static void initializeSession(ApplicationContext context) {
		KieSessionService kieSession = context.getBean(KieSessionService.class);
		kieSession.insert(MaliciousIPHandler.ips);
		// provera da li radi
		Test t = new Test();
		kieSession.setAgendaFocus("test-hospital");
		kieSession.insert(t);
		kieSession.fireAllRules();
	}

}
