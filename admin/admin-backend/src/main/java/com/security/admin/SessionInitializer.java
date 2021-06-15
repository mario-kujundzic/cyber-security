package com.security.admin;

import org.springframework.context.ApplicationContext;

import com.security.admin.model.drools.Test;
import com.security.admin.service.KieSessionService;

public class SessionInitializer {
	public static void initializeSession(ApplicationContext context) {
		KieSessionService kieSession = context.getBean(KieSessionService.class);
		initializeGlobals(kieSession);
		
		// na ovu foru ce se stavljati u servisima gde treba da se okine pravilo
		Test t = new Test();
		t.setTest("nesto");
		kieSession.insert(t);
		kieSession.setAgendaFocus("test-admin");
		kieSession.fireAllRules();
		//addJobSeekersToContext(context, kieSession);
	}

	private static void initializeGlobals(KieSessionService session) {
		
	}

	//model kako dodavati stvari u kontekst ako zatreba
//	private static void addJobSeekersToContext(ApplicationContext context, KieSessionService session) {
//		JobSeekerRepository repo = context.getBean(JobSeekerRepository.class);
//		List<JobSeeker> jobSeekers = repo.findAll();
//		for (JobSeeker js : jobSeekers) {
//			session.insert(js);
//		}
//	}
}
