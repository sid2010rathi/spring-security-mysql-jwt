package com.example.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.security.filter.JWTFilter;

@EnableWebSecurity
public class AuthenticationConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired UserDetailsService userDetailsService;
	
	@Autowired JWTFilter jwtFilter;
	
	//Authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {		
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//Authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		 * http.csrf().disable() .authorizeRequests()
		 * .antMatchers("/authenticate").permitAll()
		 * .antMatchers("/admin").hasRole("ADMIN")
		 * .antMatchers("/user").hasAnyRole("USER", "ADMIN")
		 * .antMatchers("/").permitAll() .and().formLogin();
		 */
		
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/authenticate", "/register", "/").permitAll()
			.anyRequest().authenticated()
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		 
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
