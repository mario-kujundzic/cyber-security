//package com.security.admin;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@Order(Ordered.LOWEST_PRECEDENCE)
//public class CertificateAuthFilter extends X509AuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    @Lazy(true)
//    public CertificateAuthFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//	@Override
//	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//			throws IOException, ServletException {
//
//		HttpServletRequest request = (HttpServletRequest) req;
//		HttpServletResponse response = (HttpServletResponse) res;
//		
//		String[] origins = {"https://localhost:8081", "https://localhost:9002" };
//		for (String s : origins) {
//			if (request.getHeader("Origin") != null) {
//				if (s.equals(request.getHeader("Origin"))) {
//					response.setHeader("Access-Control-Allow-Origin", s);		
//					break;
//				}				
//			}
//		}
//		chain.doFilter(req, res);
//	}
//
//}
