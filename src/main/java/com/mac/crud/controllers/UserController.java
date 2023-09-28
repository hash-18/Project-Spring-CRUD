package com.mac.crud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mac.crud.configuration.util.Authenticated;
import com.mac.crud.dtos.ApiResponse;
import com.mac.crud.dtos.UserDto;
import com.mac.crud.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/records")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public ResponseEntity<List<UserDto>> findAll() {
		List<UserDto> userDtos = this.userService.findAll();
		return new ResponseEntity<>(userDtos, HttpStatus.OK);
	}

	@PreAuthorize("permitAll()")
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int id) {
		UserDto userDto = this.userService.findById(id);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@Authenticated
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto savedUserDto = this.userService.saveUser(userDto);
		return new ResponseEntity<UserDto>(savedUserDto, HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	@Authenticated
	public ResponseEntity<UserDto> updateUser(@PathVariable int id, @RequestBody UserDto userDto) {
		UserDto updatedUser = this.userService.updateUser(id, userDto);
		return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Authenticated
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable int id) {
		this.userService.deleteUser(id);
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(String.format("Record with ID %s has been deleted", id), true), HttpStatus.OK);
	}
}
