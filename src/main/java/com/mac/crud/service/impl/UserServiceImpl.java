package com.mac.crud.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mac.crud.entities.User;
import com.mac.crud.dtos.UserDto;
import com.mac.crud.exceptions.UserNotFoundException;
import com.mac.crud.repositories.UserRepository;
import com.mac.crud.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto saveUser(UserDto userDto ) {
		
		User user=this.modelMapper.map(userDto, User.class);
		User savedUser=this.userRepository.save(user);
		System.out.println("Id before mapping "+ savedUser.getId());
		UserDto savedUserDto= this.modelMapper.map(savedUser, UserDto.class);
		System.out.println("Id after mapping in dto "+ savedUserDto.getId());
		return savedUserDto;
	}

	@Override
	public UserDto findById(Integer id) {
		
		User user=this.userRepository.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User","Id",id));
		
		return this.modelMapper.map(user, UserDto.class);
		
	}

	@Override
	public List<UserDto> findAll() {
		
		List<User> users=this.userRepository.findAll();
		
		List<UserDto> usersDto=users.stream().map(user->this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
		return usersDto;
	}

	@Override
	public UserDto updateUser(int id,UserDto userDto ) {
		
		User user=this.userRepository.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User","Id",id));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAge(userDto.getAge());
		
		this.userRepository.save(user);
		
		UserDto updatedUserDto=this.modelMapper.map(user, UserDto.class);
		return updatedUserDto;
		
	}

	@Override
	public void deleteUser(int id) {
		User user=this.userRepository.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User","Id",id));
		
		this.userRepository.delete(user);
	}

}
