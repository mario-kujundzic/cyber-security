package com.security.hospital;

import com.google.gson.Gson;
import com.security.hospital.dto.LogMessageDTO;
import com.security.hospital.enums.LogMessageType;
import com.security.hospital.pki.util.KeyIssuerSubjectGenerator;
import com.security.hospital.pki.util.PEMUtility;
import com.security.hospital.service.LogService;
import com.security.hospital.util.ValidationUtility;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.regex.Pattern;

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

}
