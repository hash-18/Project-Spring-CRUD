package com.mac.crud.filter;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mac.crud.service.impl.CustomUserDetailsService;
import com.mac.crud.service.impl.JwtService;
import com.mac.crud.service.impl.TokenBlacklistService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private TokenBlacklistService tokenBlacklistService;
	
	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, ExpiredJwtException, RuntimeException {
		String authHeader = request.getHeader("Authorization");
				
		
		String token = null;
		String userName = null;
		if (authHeader != null && authHeader.startsWith("Bearer") && !this.tokenBlacklistService.isTokenBlacklisted(token)) {
			token = authHeader.substring(7);
			try {
				if(jwtService.isTokenExpired(token))
					throw new ExpiredJwtException(null, null, userName);
					//throw new RuntimeException("JWT token has expired");
				userName = jwtService.extractUsername(token);
				
			} catch (IllegalArgumentException e) {
				logger.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
			} catch (ExpiredJwtException e) {
				
				logger.info("Given jwt token is expired !!");
				e.printStackTrace();
			} catch (MalformedJwtException e) {
				logger.info("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if (jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			else
			 logger.info("Validation fails !!");
		}
		filterChain.doFilter(request, response);

	}
}
