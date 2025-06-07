package com.whatsapp.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.auth.dto.Login;
import com.whatsapp.auth.dto.Register;
import com.whatsapp.auth.dto.User;
import com.whatsapp.auth.repository.UserRepository;
import com.whatsapp.auth.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody Register registerDto) {
		if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
			return ResponseEntity.badRequest().body("Username is already taken");
		}
		User user = new User(registerDto.getUsername(), passwordEncoder.encode(registerDto.getPassword()));
		userRepository.save(user);
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody Login loginDto) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

		final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(jwt);
	}
}