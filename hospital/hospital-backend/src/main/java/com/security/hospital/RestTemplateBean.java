package com.security.hospital;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateBean {	
//	private String certPath = "./src/main/resources/keystore/cert_auth_store.jks";
	private String certPath = "./src/main/resources/keystore/keystore.p12";
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
	    char[] password = "sadpotato".toCharArray();
	    KeyStore keyStore = keyStore(certPath, password);
	    SSLContext sslContext = SSLContextBuilder.create()
	            .loadKeyMaterial(keyStore, password)
	            .loadTrustMaterial(new TrustSelfSignedStrategy()).build();
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
	    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
	    return new RestTemplate(requestFactory);
	}

	 private KeyStore keyStore(String file, char[] password) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
	    File key = ResourceUtils.getFile(file);
	    try (InputStream in = new FileInputStream(key)) {
	        keyStore.load(in, password);
	    }
	    return keyStore;
	}
}
