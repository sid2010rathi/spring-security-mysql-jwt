package com.example.security.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.security.models.MyUserDetails;
import com.example.security.models.User;
import com.example.security.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired private UserRepository userRepository;
	
	@Autowired private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user == null)
			throw new UsernameNotFoundException("Not found : "+username);
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}
	
	public User save(MyUserDetails userDetails) {
		User user = new User();
		user.setUsername(userDetails.getUsername());
		user.setPassword(bcryptEncoder.encode(userDetails.getPassword()));
		return userRepository.save(user);
	}

}
