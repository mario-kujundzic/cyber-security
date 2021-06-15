package com.security.hospital;

import org.springframework.context.ApplicationContext;

import com.security.hospital.service.KieSessionService;

public class SessionInitializer {
	public static void initializeSession(ApplicationContext context) {
		KieSessionService kieSession = context.getBean(KieSessionService.class);
		initializeGlobals(kieSession);
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
