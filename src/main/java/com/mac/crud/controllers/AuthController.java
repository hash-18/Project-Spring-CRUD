package com.mac.crud.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mac.crud.entities.AdminUser;
import com.mac.crud.service.AuthService;
import com.mac.crud.service.impl.JwtService;
import com.mac.crud.service.impl.TokenBlacklistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private TokenBlacklistService tokenBlacklistService;
		
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@PostMapping("/register")
	public ResponseEntity<AdminUser> register(@Valid @RequestBody AdminUser adminUser)
	{
		System.out.println(adminUser);
		AdminUser savedUser=this.authService.save(adminUser);
		return new ResponseEntity<AdminUser>(savedUser,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String,String>> returnToken(@RequestBody AdminUser adminUser)
	{
		this.authService.doAuthenticate(adminUser.getUserName(), adminUser.getPassword());
		UserDetails userDetails=this.userDetailsService.loadUserByUsername(adminUser.getUserName());
		Map<String,String> tokenMap=new HashMap<>();
		String token = this.jwtService.generateToken(userDetails.getUsername());
		tokenMap.put("token", token);
		return new ResponseEntity<>(tokenMap,HttpStatus.OK);
	}
	
	@GetMapping("/validate")
	public ResponseEntity<Map<String,String>> validateToken(@RequestHeader("Authorization") String token)
	{
		System.out.println(token);
		String message=null;
		if(this.authService.validateToken(token))
			message="Token is valid";
		else
			message="Token is invalid";
		Map<String,String> result=new HashMap<>();
		result.put("message", message);
		return new ResponseEntity<>(result, HttpStatus.OK);
			
	}
	
	@PostMapping("/logout")
public ResponseEntity<Map<String,String>> logOut(@RequestHeader("Authorization") String token){
	Map<String,String> result=new HashMap<>();
	this.tokenBlacklistService.blacklistToken(token);
	result.put("message", "User Logged out");
	return new ResponseEntity<>(result, HttpStatus.OK);
		
	
}
	

}
