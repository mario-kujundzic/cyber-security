package com.security.admin.service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.security.admin.dto.UserTokenStateDTO;
import com.security.admin.exception.UserException;
import com.security.admin.model.User;
import com.security.admin.repository.UserRepository;
import com.security.admin.security.CustomUserDetailsService;
import com.security.admin.security.TokenUtils;

@Service
public class UserService {

    private UserRepository userRepository;
    private TokenUtils tokenUtils;
    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService userDetailsService;
//    private AuthorityService authorityService;
//    private MailSenderService mailSenderService;

    @Autowired
    public UserService(UserRepository userRepository, TokenUtils tokenUtils, AuthenticationManager authenticationManager,
                       CustomUserDetailsService userDetailsService/*, AuthorityService authorityService,
                       MailSenderService mailSenderService*/) {
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
//        this.authorityService = authorityService;
//        this.mailSenderService = mailSenderService;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
//
//    public AccountDTO getAccount(Long id) {
//        User user = userRepository.findById(id).get();
//        AccountDTO dto = new AccountDTO(user.getId(), user.getName(), user.getSurname(), user.getUsername());
//        return dto;
//    }
//
//    public User findByKey(String key) {
//        return userRepository.findByKey(key);
//    }
//
//    public UserDTO create(UserDTO dto) throws UniqueConstraintViolationException {
//        checkUnique(dto);
//        RegisteredUser user = toEntity(dto);
//        createUserAuthority(user, "ROLE_USER");
//        createUserKey(user);
//        save(user);
//        return toDTO(user);
//    }
//
//    public UserDTO createConfirmed(UserDTO dto) throws UniqueConstraintViolationException {
//        checkUnique(dto);
//        RegisteredUser user = toEntity(dto);
//        createUserAuthority(user, "ROLE_USER");
//        user.setEnabled(true);
//        save(user);
//        return toDTO(user);
//    }
//    
//    public UserDTO createWithResetKey(UserDTO dto) throws UniqueConstraintViolationException {
//    	checkUnique(dto);
//    	RegisteredUser user = toEntity(dto);
//    	createUserAuthority(user, "ROLE_USER");
//    	user.setEnabled(true);
//    	user.setResetKey("test-key");
//    	save(user);
//    	return toDTO(user);
//    }
//
//    public void createUserAuthority(User user, String role) {
//        user.setPassword(userDetailsService.encodePassword(user.getPassword()));
//        ArrayList<Authority> auth = new ArrayList<>();
//        auth.add(authorityService.findByName(role));
//        user.setAuthorities(auth);
//    }
//
//    public void createUserKey(User user) {
//        String generatedKey = RandomUtil.buildAuthString(30);
//        user.setKey(generatedKey);
//        mailSenderService.confirmRegistration(user.getUsername(), generatedKey);
//    }
//
//    public User save(User user) {
//        return userRepository.save(user);
//    }
//
//    public void delete(String username) throws NoSuchElementException {
//        User user = getOne(username);
//        userRepository.delete(user);
//    }
//

	public UserTokenStateDTO login(String username, String password) throws UserException {
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
	
    public User getOne(String username) throws NoSuchElementException {
        User user = findByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User with this username doesn't exist!");
        }
        return user;
    }
    
    public UserTokenStateDTO generateToken(String username, String password) throws UserException {
        Authentication authentication = null;
        try {
            authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new UserException("Bad credentials exception!", "password", "Incorrect password.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // create token
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        return new UserTokenStateDTO(user.getId(), jwt, expiresIn);
    }
//
//    public UserDTO changePassword(String oldPassword, String newPassword) {
//        String user = userDetailsService.changePassword(oldPassword, newPassword);
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername(user);
//        return userDTO;
//    }
//
//    public UserTokenStateDTO resetPassword(ResetPasswordDTO dto) throws UserException {
//        User user = userRepository.findByResetKey(dto.getResetKey());
//        if (user == null) {
//            throw new NoSuchElementException("The password is already reset or the link is invalid!");
//        }
//        userDetailsService.changePasswordUtil(user, dto.getNewPassword());
//        user.setResetKey(null);
//        save(user);
//
//        UserTokenStateDTO token = generateToken(user.getUsername(), dto.getNewPassword());
//        return token;
//    }
//
//    public UserDTO confirmRegistration(String key) throws NoSuchElementException {
//        User user = userRepository.findByKey(key);
//        if (user == null) {
//            throw new NoSuchElementException("User already activated or doesn't exist!");
//        }
//        user.setEnabled(true);
//        user.setKey(null);
//        save(user);
//        return toDTO(user);
//    }
//
//    public void forgotPassword(String username) {
//        User user = getOne(username);
//        if (!user.isEnabled()) {
//            throw new DisabledException("Your account hasn't been activated yet. Please check your email first!");
//        }
//        String generatedKey = RandomUtil.buildAuthString(30);
//        user.setResetKey(generatedKey);
//        mailSenderService.forgotPassword(user.getUsername(), generatedKey);
//        save(user);
//    }
//
//    public void update(Long id, AccountDTO dto) {
//        User user = userRepository.findById(id).get();
//        user.setName(dto.getName());
//        user.setSurname(dto.getSurname());
//        save(user);
//    }
//

//
//    private RegisteredUser toEntity(UserDTO dto) {
//        RegisteredUser user = new RegisteredUser(dto.getName(), dto.getSurname(), dto.getUsername(), dto.getPassword());
//        return user;
//    }
//
//    private UserDTO toDTO(User user) {
//        UserDTO dto = new UserDTO();
//        dto.setId(user.getId());
//        dto.setUsername(user.getUsername());
//        dto.setName(user.getName());
//        dto.setSurname(user.getSurname());
//        return dto;
//    }
//
//    private User checkUnique(UserDTO dto) throws UniqueConstraintViolationException {
//        User user = userRepository.findByUsername(dto.getUsername());
//        if (user != null) {
//            // register
//            if (dto.getId() == null) {
//                throw new UniqueConstraintViolationException("Unique key constraint violated!", "username",
//                        "User with this username already exists!");
//            } else {
//                // update profile
//                if (!user.getId().equals(dto.getId())) {
//                    throw new UniqueConstraintViolationException("Unique key constraint violated!", "username",
//                            "User with this username already exists!");
//                } else {
//                    return user;
//                }
//            }
//        }
//        return user;
//    }


}
