package com.security.admin.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.security.admin.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Utility klasa za rad sa JSON Web Tokenima
@Component
public class TokenUtils {

	// Izdavac tokena
	@Value("spring-security-example")
	private String APP_NAME;

	// Tajna koju samo backend aplikacija treba da zna kako bi mogla da generise i
	// proveri JWT https://jwt.io/
	@Value("somesecret")
	public String SECRET;

	// Period vazenja
	@Value("43200000")

	private int EXPIRES_IN;

	// Naziv headera kroz koji ce se prosledjivati JWT u komunikaciji server-klijent
	@Value("Authorization")
	private String AUTH_HEADER;

	// Moguce je generisati JWT za razlicite klijente (npr. web i mobilni klijenti
	// nece imati isto trajanje JWT, JWT za mobilne klijente ce trajati duze jer se
	// mozda aplikacija redje koristi na taj nacin)
	private static final String AUDIENCE_WEB = "web";
	private static final String AUDIENCE_MOBILE = "mobile";
	private static final String AUDIENCE_TABLET = "tablet";

	// Algoritam za potpisivanje JWT
	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
//	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.ES256;

	private SecureRandom secureRandom = new SecureRandom();

	// Funkcija za generisanje JWT token
	public HashMap<String, String> generateToken(String username) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String cookie = generateCookie();
		String fingerprint = generateFingerprint(cookie);

		// token structure [Base64(HEADER)].[Base64(PAYLOAD)].[Base64(SIGNATURE)]
		String jwt = Jwts.builder().setHeaderParam("alg", "HS512").setHeaderParam("typ", "JWT").setIssuer(APP_NAME)
				.setSubject(username).setAudience(generateAudience()).setIssuedAt(new Date())
				.setExpiration(generateExpirationDate()) // TODO: MNOGO KRACI EXP DATE
				.claim("userFingerprint", fingerprint).signWith(SIGNATURE_ALGORITHM, SECRET).compact();

		HashMap<String, String> token = new HashMap<>();
		token.put("jwt", jwt);
		token.put("cookie", cookie);

		return token;

		// stara verzija
//		return Jwts.builder()
//				
//				.setIssuer(APP_NAME)
//				.setSubject(username)
//				.setAudience(generateAudience())
//				.setIssuedAt(new Date())
//				.setExpiration(generateExpirationDate())
//				// .claim("key", value) //moguce je postavljanje proizvoljnih podataka u telo JWT tokena
//				.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
	}

	private String generateFingerprint(String cookie) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		// Compute a SHA256 hash of the fingerprint in order to store the fingerprint
		// hash (instead of the raw value) in the token
		// to prevent an XSS to be able to read the fingerprint and set the expected
		// cookie itself
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] userFingerprintDigest = digest.digest(cookie.getBytes("utf-8"));
		String userFingerprintHash = DatatypeConverter.printHexBinary(userFingerprintDigest);

		return userFingerprintHash;
	}
	
	private String generateCookie() {
		byte[] randomFgp = new byte[50];
		secureRandom.nextBytes(randomFgp);
		String cookie = DatatypeConverter.printHexBinary(randomFgp);
		
		return cookie;
		
	}

	private String generateAudience() {
//		Moze se iskoristiti org.springframework.mobile.device.Device objekat za odredjivanje tipa uredjaja sa kojeg je zahtev stigao.

//		String audience = AUDIENCE_UNKNOWN;
//		if (device.isNormal()) {
//			audience = AUDIENCE_WEB;
//		} else if (device.isTablet()) {
//			audience = AUDIENCE_TABLET;
//		} else if (device.isMobile()) {
//			audience = AUDIENCE_MOBILE;
//		}
		return AUDIENCE_WEB;
	}

	private Date generateExpirationDate() {
		return new Date(new Date().getTime() + EXPIRES_IN);
	}

	// Funkcija za refresh JWT tokena
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			claims.setIssuedAt(new Date());
			refreshedToken = Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
					.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = this.getIssuedAtDateFromToken(token);
		return (!(this.isCreatedBeforeLastPasswordReset(created, lastPasswordReset))
				&& (!(this.isTokenExpired(token)) || this.ignoreTokenExpiration(token)));
	}

	// Funkcija za validaciju JWT tokena
	public Boolean validateToken(String token, String cookie, UserDetails userDetails) {
		Claims claims = getAllClaimsFromToken(token);
		User user = (User) userDetails;
		final String username = claims.getSubject();
		final Date created = claims.getIssuedAt();
		final String issuer = claims.getIssuer();
		final JwsHeader header = getHeaderFromToken(token);

		
		String fingerprintFromCookie = null;
		try {
			fingerprintFromCookie = generateFingerprint(cookie);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String fingerprintFromJwt = (String) claims.get("userFingerprint");
		
		return (username != null && username.equals(userDetails.getUsername()) && issuer.equals(APP_NAME)
				&& header.get("alg").equals("HS512") && fingerprintFromCookie.equals(fingerprintFromJwt)
				&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			audience = claims.getAudience();
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	public int getExpiredIn() {
		return EXPIRES_IN;
	}

	// Funkcija za preuzimanje JWT tokena  iz zahteva
	public String getToken(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromHeader(request);

		// JWT se prosledjuje kroz header Authorization u formatu:
		// Bearer
		// eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}
	
	public String getCookieFromHeader(HttpServletRequest request) {
		String cookie = request.getHeader("Cookie").split("=")[1];
		return cookie;

	}
	
	

//	public String getCookieFromHeader(HttpServletRequest request) {
//		// Retrieve the user fingerprint from the dedicated cookie
//		String biscuit = null;
//		if (request.getCookies() != null && request.getCookies().length > 0) {
//			List<Cookie> cookies = Arrays.stream(request.getCookies()).collect(Collectors.toList());
//			Optional<Cookie> cookie = cookies.stream().filter(c -> "__Secure-Fgp".equals(c.getName())).findFirst();
//			if (cookie.isPresent()) {
//				biscuit = cookie.get().getValue();
//			}
//		}
//		return biscuit;
//	}
	

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = this.getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		String audience = this.getAudienceFromToken(token);
		return (audience.equals(AUDIENCE_TABLET) || audience.equals(AUDIENCE_MOBILE));
	}

	// Funkcija za citanje svih podataka iz JWT tokena
	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	@SuppressWarnings("rawtypes")
	private JwsHeader getHeaderFromToken(String token) {
		JwsHeader header;
		try {
			header = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getHeader();
		} catch (Exception e) {
			header = null;
		}
		return header;
	}

}