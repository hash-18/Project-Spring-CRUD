package com.mac.crud.configuration.util;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        
        
        
        writer.println("Access Denied !! " + authException.getMessage());
		
//		if (authException.getClass().equals(ExpiredJwtException.class)) {
//			 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	            PrintWriter writer = response.getWriter();
//	            writer.println("JWT expired");
//		}
//		else
//		{
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            PrintWriter writer = response.getWriter();
//            writer.println("Access Denied !! " + authException.getMessage());
//		}
    }

}
