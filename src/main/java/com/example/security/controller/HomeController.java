package com.example.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.models.AuthenticationRequest;
import com.example.security.models.AuthenticationResponse;
import com.example.security.models.MyUserDetails;
import com.example.security.service.MyUserDetailsService;
import com.example.security.utility.JWTUtil;

@RestController
public class HomeController {
	
	@Autowired private AuthenticationManager authenticationManager;
	
	@Autowired private MyUserDetailsService userdetailsService;
	
	@Autowired private JWTUtil jwtUtil;

	@GetMapping("/")
	public String home() {
		return "<h1>Welcome!! This is Home.</h1>";
	}
	
	@GetMapping("/user") public String user() { return "<h1>Welcome, user!! This is user page.</h1>"; }
	
	@GetMapping("/admin") public String admin() { return "<h1>Welcome, admin!! This is admin page.</h1>"; }
	 
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		final UserDetails userDetails = userdetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody MyUserDetails userDetails) throws Exception {
		
		return ResponseEntity.ok(userdetailsService.save(userDetails));
		
	}
}
