package com.security.hospital;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HospitalApplication {

	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		ApplicationContext context = SpringApplication.run(HospitalApplication.class, args);
		FirstTimeSetup.execute();

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
//		logService.logDeviceInfo("d1", "Some device info");
//		logService.logDeviceInfo("d2", "Some device info");
//		logService.logDeviceError("d1", "A device error");
//		logService.logDeviceInfo("d1", "Some device info");
//		logService.logDeviceInfo("d2", "Some device info");
//		logService.logGeneralWarning("A General warning");
//		logService.logGeneralError("Some General error");
//		logService.logGeneralInfo("Some General info");
//		logService.logGeneralInfo("Some more General info");
//		logService.logGeneralWarning("A General warning");
//		logService.logDeviceInfo("d3", "Some device info");
//		logService.logDeviceError("d1", "A device error");
//		logService.logDeviceInfo("d1", "Some device info");
//		logService.logGeneralError("Some General error");
//		logService.logGeneralInfo("Some General info");
//		logService.logGeneralInfo("Some more General info");
//		logService.logGeneralWarning("A General warning");
//		logService.logDeviceInfo("d2", "Some device info");
//		logService.logDeviceInfo("d3", "Some device info");
//		logService.logGeneralInfo("Some General info");
//		logService.logGeneralInfo("Some more General info");
//		logService.logDeviceError("d1", "A device error");
//		logService.logDeviceInfo("d1", "Some device info");
//
//		logService.logDeviceInfo("d3", "Some device info");
//		logService.logDeviceError("d1", "A device error");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logDeviceInfo("d2", "Some device info");
//		logService.logDeviceInfo("d3", "Some device info");
//		logService.logDeviceError("d1", "A device error");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthError("Some auth error");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logDeviceInfo("d1", "Some device info");
//		logService.logDeviceInfo("d2", "Some device info");
//
//		logService.logGeneralError("Some General error");
//		logService.logGeneralWarning("A General warning");
//		logService.logDeviceInfo("d3", "Some device info");
//		logService.logDeviceError("d1", "A device error");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthWarning("Some auth warning");
//		logService.logAuthError("Some auth error");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logAuthInfo("Some auth info");
//		logService.logDeviceInfo("d1", "Some device info");
//		logService.logDeviceInfo("d2", "Some device info");
//		logService.logDeviceInfo("d3", "Some device info");
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
				.newKieContainer(ks.newReleaseId("sbnz.integracija", "security-spring-kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		return kContainer;
	}

}
