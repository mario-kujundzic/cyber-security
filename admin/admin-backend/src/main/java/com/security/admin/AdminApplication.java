package com.security.admin;

import java.security.Security;
import java.util.regex.Pattern;

import com.security.admin.service.LogService;
import com.security.admin.util.ValidationUtility;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		ApplicationContext context = SpringApplication.run(AdminApplication.class, args);
		FirstTimeSetup.execute();
		SessionInitializer.initializeSession(context);
//		LogService logService = context.getBean(LogService.class);
//
//		logService.logGeneralInfo("Some General info");
//		logService.logGeneralInfo("Some more General info");
//		logService.logGeneralWarning("A General warning");
//		logService.logGeneralError("Some General error");
//		logService.logGeneralInfo("Some General info");
//		logService.logGeneralInfo("Some more General info");
//		logService.logGeneralError("Some General error");
//
//		logService.logGeneralWarning("A General warning");
//		logService.logGeneralError("Some General error");
//		logService.logGeneralInfo("Some General info");
//		logService.logGeneralInfo("Some more General info");
//		logService.logGeneralWarning("A General warning");
//		logService.logGeneralError("Some General error");
//		logService.logGeneralInfo("Some General info");
//		logService.logGeneralInfo("Some more General info");
//		logService.logGeneralWarning("A General warning");
//		logService.logGeneralInfo("Some General info");
//		logService.logGeneralInfo("Some more General info");
//
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthError("Some auth error");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//
//		logService.logGeneralError("Some General error");
//		logService.logGeneralWarning("A General warning");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthError("Some auth error");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthError("Some auth error");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthError("Some auth error");
	}
	
	@Bean
	public KieContainer kieContainer() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("sbnz.integracija", "security-admin-spring-kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		return kContainer;
	}

}
