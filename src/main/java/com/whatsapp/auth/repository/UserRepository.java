package com.whatsapp.auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.whatsapp.auth.dto.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	Optional<User> findByUsername(String username);
	
}
