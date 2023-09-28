package com.mac.crud.exceptions;

import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mac.crud.dtos.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.DecodingException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex) {
		ApiResponse apiResponse = new ApiResponse(ex.getMessage(), false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
		Map<String, String> resp = new HashMap<>();

		ex.getConstraintViolations().forEach(error -> {
			String fieldName = error.getPropertyPath().toString();
			String message = error.getMessage();
			resp.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		 Map<String, String> errors = new HashMap<>();
		  ex.getBindingResult().getFieldErrors().forEach(error -> {
		    String fieldName = error.getField();
		    String message = error.getDefaultMessage();
		    errors.put(fieldName, message);
		  });

		return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);

	}


	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return new ResponseEntity<String>("Username already registered", HttpStatus.BAD_REQUEST);

	}
	@ExceptionHandler({SignatureException.class,DecodingException.class,ExpiredJwtException.class})
	public ResponseEntity<Map<String,String>> jwtExceptionHandler(Exception e)
	{
		Map<String,String> result=new HashMap<>();
		result.put("message", "Invalid token");
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String,String>> handleRunTimeException(Exception e)
	{
		Map<String,String> result=new HashMap<>();
		result.put("message", "Invalid request");
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}
}
