package com.security.admin;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.security.admin.security.CustomUserDetailsService;
import com.security.admin.security.TokenUtils;
import com.security.admin.security.auth.RestAuthenticationEntryPoint;
import com.security.admin.security.auth.TokenAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//    @Autowired
//    private CertificateAuthFilter certAuthFilter;

	@Autowired
	private CustomUserDetailsService jwtUserDetailsService;

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Autowired
	private TokenUtils tokenUtils;

//	@Autowired
//	public AdminSecurityConfiguration(CustomUserDetailsService jwtUserDetailsService,
//			RestAuthenticationEntryPoint restAuthenticationEntryPoint, TokenUtils tokenUtils) {
//		this.jwtUserDetailsService = jwtUserDetailsService;
//		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
//		this.tokenUtils = tokenUtils;
//	}

	// Registrujemo authentication manager koji ce da uradi autentifikaciju
	// korisnika za nas
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	// Definisemo uputstvo za authentication managera koji servis da koristi da
	// izvuce podatke o korisniku koji zeli da se autentifikuje,
	// kao i kroz koji enkoder da provuce lozinku koju je dobio od klijenta u
	// zahtevu da bi adekvatan hash koji dobije kao rezultat bcrypt algoritma
	// uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain
	// lozinka)
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	// Definisemo prava pristupa odredjenim URL-ovima
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		CorsConfiguration config = new CorsConfiguration();
//		config.setAllowedOriginPatterns(
//				Arrays.asList("https://localhost:*", "https://localhost:*/*", "https://localhost:*/"));
//		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//		config.setAllowedHeaders(Arrays.asList("*"));
//		config.setExposedHeaders(Arrays.asList("*"));
//		config.setAllowCredentials(true);

		http
				// komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST
				// aplikacija
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				// sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
				.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

				// svim korisnicima dopusti da pristupe putanjama /auth/**, (/h2-console/** ako
				// se koristi H2 baza) i /api/foo
				.authorizeRequests().antMatchers("/auth/**").permitAll()

				// za svaki drugi zahtev korisnik mora biti autentifikovan
				.anyRequest().authenticated().and()
//				.cors().configurationSource(corsConfigurationSource()).and()

				// umetni custom filter TokenAuthenticationFilter kako bi se vrsila provera JWT
				// tokena umesto cistih korisnickog imena i lozinke (koje radi
				// BasicAuthenticationFilter)
				.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, jwtUserDetailsService),
						BasicAuthenticationFilter.class)
//				;
				.x509().subjectPrincipalRegex("0.9.2342.19200300.100.1.1=(.*)");
//				.x509AuthenticationFilter(certAuthFilter);
		// zbog jednostavnosti primera
		http.csrf().disable();
	}

//	@Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//		config.setAllowedOriginPatterns(
//				Arrays.asList("https://localhost:*", "https://localhost:*/*", "https://localhost:*/"));
//		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//		config.setAllowedHeaders(Arrays.asList("*"));
//		config.setExposedHeaders(Arrays.asList("*"));
//		config.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

	// Generalna bezbednost aplikacije
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TokenAuthenticationFilter ce ignorisati sve ispod navedene putanje
		web.ignoring().antMatchers(HttpMethod.POST, "/auth/login", "/auth/register", "/api/deleteUserRequests/request",
				"/api/certificateRequests/request", "/api/modifyUserRequests/request", "/api/addUserRequests/request",
				"/api/certificates/requestRevoke");
		web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html",
				"/**/*.css", "/**/*.js", "/api/certificates/status/*");
	}
}