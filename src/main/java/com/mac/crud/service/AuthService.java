package com.mac.crud.service;

import com.mac.crud.entities.AdminUser;

public interface AuthService {
	
	AdminUser save(AdminUser adminUser);
	void doAuthenticate(String userName, String password);
	boolean validateToken(String token);
	

}
