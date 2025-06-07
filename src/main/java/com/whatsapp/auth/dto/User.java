package com.whatsapp.auth.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "users")
@Data
public class User {

	@Id
	private String id;
	private String username;
	private String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
