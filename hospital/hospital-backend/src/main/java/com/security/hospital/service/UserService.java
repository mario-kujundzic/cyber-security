package com.security.hospital.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;
import java.util.stream.Stream;

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

import com.security.hospital.dto.AddUserRequestDTO;
import com.security.hospital.dto.DeleteUserRequestDTO;
import com.security.hospital.dto.ModifyUserRequestDTO;
import com.security.hospital.dto.UserDTO;
import com.security.hospital.dto.ResetPasswordDTO;
import com.security.hospital.dto.UserTokenStateDTO;
import com.security.hospital.exceptions.OftenUsedPasswordException;
import com.security.hospital.exceptions.UserException;
import com.security.hospital.model.Admin;
import com.security.hospital.model.Doctor;
import com.security.hospital.model.Role;
import com.security.hospital.model.User;
import com.security.hospital.repository.RoleRepository;
import com.security.hospital.repository.UserRepository;
import com.security.hospital.security.TokenUtils;
import com.security.hospital.util.RandomUtility;

@Service
public class UserService implements UserDetailsService {

	private UserRepository userRepository;
	private TokenUtils tokenUtils;
	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	private RoleRepository roleRepository;
	private MailSenderService mailSenderService;

	@Autowired
	public UserService(UserRepository userRepository, TokenUtils tokenUtils,
			AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
			MailSenderService mailSenderService, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.tokenUtils = tokenUtils;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.mailSenderService = mailSenderService;
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public UserTokenStateDTO login(String username, String password) throws DisabledException, UserException {
		User existUser = null;
		try {
			existUser = getOne(username);
		} catch (NoSuchElementException e) {
			throw new UserException("No such element!", "username", "User with this username doesn't exist.");
		}

		if (!existUser.isEnabled()) {
			throw new DisabledException("Your account hasn't been activated yet. Please check your email!");
		}

		UserTokenStateDTO token = generateToken(username, password);
		return token;

	}

	public UserTokenStateDTO generateToken(String username, String password) throws UserException {
		Authentication authentication = null;
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (BadCredentialsException e) {
			throw new UserException("Bad credentials exception!", "password", "Incorrect password.");
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		// create token
		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername());
		long expiresIn = tokenUtils.getExpiredIn();

		return new UserTokenStateDTO(user.getId(), jwt, expiresIn, user.getUsername(), user.getName(),
				user.getSurname(), user.getRole());
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

	public void forgotPassword(String username) {
		User user = getOne(username);
		if (!user.isEnabled()) {
			throw new DisabledException("Your account hasn't been activated yet. Please check your email first!");
		}
		String generatedKey = RandomUtility.buildAuthString(30);
		user.setResetKey(generatedKey);
		mailSenderService.forgotPassword(user.getUsername(), generatedKey);
		userRepository.save(user);
	}

	public UserTokenStateDTO resetPassword(ResetPasswordDTO dto) throws UserException, OftenUsedPasswordException {
		User user = userRepository.findByResetKey(dto.getResetKey());
		if (user == null) {
			throw new NoSuchElementException("The password is already reset or the link is invalid!");
		}
		String newPassword = dto.getNewPassword();
		if (oftenUsedPassword(newPassword)) {
			throw new OftenUsedPasswordException("The password is often used and therefore weak!");
		}
		if (passwordContainsUserData(newPassword, user)) {
			throw new OftenUsedPasswordException("The password should not contain your personal information!");
		}
		changePasswordUtil(user, newPassword);
		user.setResetKey(null);
		userRepository.save(user);

		UserTokenStateDTO token = generateToken(user.getUsername(), newPassword);
		return token;
	}

	private boolean oftenUsedPassword(String password) {
		List<String> oftenUsedPasswords = new ArrayList<>();
		try (Stream<String> lines = Files.lines(Paths.get("./src/main/resources/common-passwords.txt"))) {
			oftenUsedPasswords = lines.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (oftenUsedPasswords.contains(password.toLowerCase()))
			return true;
		return false;
	}

	private boolean passwordContainsUserData(String password, User user) {
		String lowerPass = password.toLowerCase();
		if (lowerPass.contains(user.getName().toLowerCase()))
			return true;
		if (lowerPass.contains(user.getSurname().toLowerCase()))
			return true;
		if (lowerPass.contains(user.getUsername().split("@")[0].toLowerCase()))
			return true;
		return false;
	}

	public List<UserDTO> getAll() {
		List<UserDTO> users = userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
		return users;
	}

	private UserDTO toDTO(User u) {
		return new UserDTO(u);
	}

	public User getOneById(Long userId) {
		return userRepository.getOne(userId);
	}

	public void deleteUser(@Valid DeleteUserRequestDTO dto) {
		User u = userRepository.getOne(dto.getUserId());
		userRepository.delete(u);
	}

	public void modifyRole(@Valid ModifyUserRequestDTO dto) {
		User u = userRepository.getOne(dto.getUserId());
		userRepository.delete(u);
		List<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findByName("ROLE_" + dto.getNewRole()));
		if (dto.getNewRole().equals("ADMIN")) {
			Admin a = new Admin(u.getName(), u.getSurname(), u.getUsername(), u.getPassword());
			a.setRoles(roles);
			a.setLastPasswordResetDate(Timestamp.from(Instant.now()));
			a.setEnabled(true);
			userRepository.save(a);
		} else {
			Doctor d = new Doctor(u.getName(), u.getSurname(), u.getUsername(), u.getPassword());
			d.setRoles(roles);
			d.setLastPasswordResetDate(Timestamp.from(Instant.now()));
			d.setEnabled(true);
			userRepository.save(d);
		}
	}

	public void addUser(@Valid AddUserRequestDTO dto) {
		if (dto.getRole().equals("ADMIN")) {
			String initPassword = "changeMe123";
			Admin a = new Admin(dto.getName(), dto.getSurname(), dto.getUsername(), initPassword);
			a.setLastPasswordResetDate(null);
			String key = RandomUtility.buildAuthString(30);
			a.setResetKey(key);
			a.setEnabled(true);
			userRepository.save(a);
			mailSenderService.resetPassword(dto.getUsername(), key);

		} else {
			String initPassword = "changeMe123";
			Doctor d = new Doctor(dto.getName(), dto.getSurname(), dto.getUsername(), initPassword);
			d.setLastPasswordResetDate(null);
			userRepository.save(d);
			String key = RandomUtility.buildAuthString(30);
			d.setResetKey(key);
			d.setEnabled(true);
			userRepository.save(d);
			mailSenderService.resetPassword(dto.getUsername(), key);
		}
	}
}
