package com.security.hospital.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.security.hospital.MaliciousIPHandler;
import com.security.hospital.events.DosEvent;
import com.security.hospital.events.DosEventStatus;
import com.security.hospital.service.KieSessionService;
import com.security.hospital.service.LogService;

@Component
public class DoSInterceptor implements HandlerInterceptor {
    @Autowired
    private LogService logService;
    
    @Autowired
    private KieSessionService session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        //logService.logHospitalInfo(request.getMethod() + " " + request.getRequestURI() + " from " + request.getRemoteAddr() + request.getRemotePort());
    	String ip = request.getRemoteAddr();
    	
    	if(MaliciousIPHandler.ips.contains(ip)) {
    		return false;
    	}
    	DosEvent de = new DosEvent(ip);
    	DosEventStatus des = new DosEventStatus(ip);
    	session.insert(de);
    	session.insert(des);
    	session.setAgendaFocus("dos-agenda");
    	session.fireAllRules();
    	if (des.isAttack()) {
    		session.removeDosEvents(ip);
    		logService.logGeneralError("DoS suspected from IP: " + ip);
    		MaliciousIPHandler.ips.add(ip);
    		MaliciousIPHandler.writeIP();
    		return false;
    	}
        return true;
    }
}
