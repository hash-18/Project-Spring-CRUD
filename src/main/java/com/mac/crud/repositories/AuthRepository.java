package com.mac.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mac.crud.entities.AdminUser;

public interface AuthRepository extends JpaRepository<AdminUser, Integer>{
	
	
	AdminUser findByUserName(String userName);

}
