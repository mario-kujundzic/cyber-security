package com.security.admin;

import java.security.Security;
import java.util.regex.Pattern;

import com.security.admin.service.LogService;
import com.security.admin.util.ValidationUtility;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		ApplicationContext context = SpringApplication.run(AdminApplication.class, args);
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

}
