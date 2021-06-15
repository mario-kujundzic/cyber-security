package com.security.hospital.service;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogSimulator {

	@Autowired
	private LogService logService;

	public void simulate(String state) {
		Timer timer = new Timer();
		if (state.equals("idle")) {
			timer.cancel();
		} else if (state.equals("normal")) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						logService.logAuthInfo("Login successful for account admin1@gmail.com");
						logService.logDeviceInfo("Hospital2", "Registered device with common name Hospital1"
								+ "for patient Pacijent Pacijentic with params temperature, blood pressure");
					}
				}, 0, 5000);
		}
		else if (state.equals("brute-force")) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					logService.logAuthInfo("Login successful for account admin1@gmail.com");
					logService.logDeviceInfo("Hospital2", "Registered device with common name Hospital1"
							+ "for patient Pacijent Pacijentic with params temperature, blood pressure");
				}
			}, 0, 5000);
			
		}
		else if (state.equals("inactive-user")) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					logService.logAuthError("Login detected for admin2@gmail.com Account was unused for 90 days. IP: 12345");
				}
			}, 0, 5000);
		}
		else if (state.equals("dos")) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					logService.logGeneralError("DOS suspected from IP: 12345");
				}
			}, 0, 1);
		}
	}

}
