package com.mac.crud.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mac.crud.entities.AdminUser;
import com.mac.crud.repositories.AuthRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AuthRepository authRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AdminUser adminUser=authRepository.findByUserName(username);
		return new User(adminUser.getUserName(), adminUser.getPassword(), new ArrayList<>());
	}

}
