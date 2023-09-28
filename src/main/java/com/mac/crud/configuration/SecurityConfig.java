package com.mac.crud.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.mac.crud.configuration.util.JwtAuthenticationEntryPoint;
import com.mac.crud.filter.JwtAuthFilter;
import com.mac.crud.service.impl.CustomUserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
//@EnableWebMvc
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
    private JwtAuthFilter authFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint point;


	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 // @Bean
//	  protected void configure(HttpSecurity http) throws Exception {
//	        http.csrf().disable()
//	            .authorizeRequests()
//	                .antMatchers("/auth/register", "/auth/login").permitAll()
//	                .anyRequest().authenticated()
//	            .and()
//	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//	        http.addFilterBefore(authFilter, JwtAuthFilter.class);
//	    }
	  
//.requestMatchers("/auth/register/**","/auth/login/**").permitAll
	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http.csrf(csrf -> csrf.disable())
	                .authorizeRequests()
	                .requestMatchers("/v3/api-docs/**").permitAll()
	                .requestMatchers("/auth/register/**").permitAll()
	                .requestMatchers("/auth/login/**").permitAll()
	                .requestMatchers("/auth/validate/**").permitAll()
	                .requestMatchers("/swagger-ui/**").permitAll()
	                .anyRequest()
	                .authenticated()
	                .and()
	                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	       http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
	        return http.build();
	  
	    }
	  
	  
	  

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
