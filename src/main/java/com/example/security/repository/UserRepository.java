package com.example.security.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.security.models.User;

public interface UserRepository extends CrudRepository<User, Integer>{

	User findByUsername(String username);
}
