package com.mac.crud.service;

import java.util.List;
import com.mac.crud.dtos.UserDto;

public interface UserService {
	
	
	UserDto saveUser(UserDto userDto);
	UserDto findById(Integer id);
	List<UserDto> findAll();
	UserDto updateUser(int id, UserDto userDto);
	void deleteUser(int id);
	

}
