package com.security.hospital.service;

import java.util.NoSuchElementException;

import com.security.hospital.security.events.LoginAttemptResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.hospital.dto.UserTokenStateDTO;
import com.security.hospital.exceptions.UserException;
import com.security.hospital.model.User;
import com.security.hospital.repository.UserRepository;
import com.security.hospital.security.TokenUtils;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SecurityEventService securityEventService;

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public UserTokenStateDTO login(String username, String password, String IPAddress) throws DisabledException, UserException {
		User existUser = null;
		try {
			existUser = getOne(username);
		} catch (NoSuchElementException e) {
			securityEventService.invokeLoginAttempt(username, LoginAttemptResult.USER_DOESNT_EXIST, IPAddress, -1);
			throw new UserException("No such element!", "username", "User with this username doesn't exist.");
		}

		if (!existUser.isEnabled()) {
			securityEventService.invokeLoginAttempt(username, LoginAttemptResult.ACCOUNT_INACTIVE, IPAddress, -1);
			throw new DisabledException("Your account hasn't been activated yet. Please check your email!");
		}

		UserTokenStateDTO token = null;
		try {
			token = generateToken(username, password);
		} catch (BadCredentialsException e) {
			securityEventService.invokeLoginAttempt(username, LoginAttemptResult.PASSWORD_INCORRECT, IPAddress,
					existUser.getLastLoginDate().getTime() / 1000);
			throw new UserException("Bad credentials exception!", "password", "Incorrect password.");
		}

		existUser.resetLastLoginDate();
		userRepository.save(existUser);
		securityEventService.invokeLoginAttempt(username, LoginAttemptResult.SUCCESS, IPAddress,
				existUser.getLastLoginDate().getTime() / 1000);
		return token;

	}

	public UserTokenStateDTO generateToken(String username, String password) throws BadCredentialsException {
		Authentication authentication = null;

		authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		// create token
		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername());
		long expiresIn = tokenUtils.getExpiredIn();

		return new UserTokenStateDTO(user.getId(), jwt, expiresIn, user.getUsername(), user.getName(), user.getSurname(), user.getRole());
	}

	public User getOne(String username) throws NoSuchElementException {
		User user = null;
		try {
			user = findByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (user == null) {
			throw new NoSuchElementException("User with this username doesn't exist!");
		}
		return user;
	}
	
	// custom user details service
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return user;
		}
	}

	// Funkcija pomocu koje korisnik menja svoju lozinku
	public String changePassword(String oldPassword, String newPassword) {

		// Ocitavamo trenutno ulogovanog korisnika
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String username = currentUser.getName();
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		User user = (User) loadUserByUsername(username);
		changePasswordUtil(user, newPassword);
		
		userRepository.save(user);
		
		return username;
	}
		
	public void changePasswordUtil(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
	}
	
	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

}
