package com.security.admin.service;

import java.util.NoSuchElementException;

import com.security.admin.security.events.LoginAttemptResult;
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
import com.security.admin.security.TokenUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private UserRepository userRepository;
    private TokenUtils tokenUtils;
    private AuthenticationManager authenticationManager;
    private SecurityEventService securityEventService;

    @Autowired
    public UserService(UserRepository userRepository, TokenUtils tokenUtils,
                       AuthenticationManager authenticationManager, SecurityEventService securityEventService) {
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.securityEventService = securityEventService;
    }

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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // create token
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        String role = user.getRoles().get(0).getName();

        return new UserTokenStateDTO(
                user.getId(), jwt, expiresIn, user.getUsername(), user.getName(), user.getSurname(), role
        );
    }

    public User getOne(String username) throws NoSuchElementException {
        User user = findByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User with this username doesn't exist!");
        }
        return user;
    }
}
