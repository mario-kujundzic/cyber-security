package com.security.hospital.interceptors;

import com.security.hospital.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DoSInterceptor implements HandlerInterceptor {
    @Autowired
    private LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        logService.logHospitalInfo(request.getMethod() + " " + request.getRequestURI() + " from " + request.getRemoteAddr() + request.getRemotePort());
        return true;
    }
}
