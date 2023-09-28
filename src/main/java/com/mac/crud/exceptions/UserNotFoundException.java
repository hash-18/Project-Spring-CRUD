package com.mac.crud.exceptions;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String resourceName, String resourceField, Integer fieldValue) {
		super(String.format("Resource %s not found for field %s : %s",resourceName,resourceField,fieldValue));
	}

}
