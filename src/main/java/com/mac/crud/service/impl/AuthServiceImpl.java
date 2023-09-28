package com.mac.crud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mac.crud.entities.AdminUser;
import com.mac.crud.repositories.AuthRepository;
import com.mac.crud.service.AuthService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.DecodingException;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private PasswordEncoder passwordEncoder;  
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public AdminUser save(AdminUser adminUser) {
		adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
		return this.authRepository.save(adminUser);

	}

	public void doAuthenticate(String userName, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, password);
		try {
			manager.authenticate(authentication);

		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(" Invalid Username or Password  !!");
		}

	}

	@Override
	public boolean validateToken(String token) {
		try {
		token=token.substring(7);
		String userName=this.jwtService.extractUsername(token);
		System.out.println(userName);
		UserDetails userDetails=this.userDetailsService.loadUserByUsername(userName);
		System.out.println(userDetails);
		return this.jwtService.validateToken(token, userDetails);
		}
		catch (DecodingException | SignatureException | ExpiredJwtException e)
		{
			return false;
		}
		
	}
}
