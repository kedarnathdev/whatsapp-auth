package com.whatsapp.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.whatsapp.auth.filter.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	// Note: UserDetailsServiceImpl is the custom UserDetailsService you created in
	// the previous step
	@Autowired
	private UserDetailsService userDetailsService;

	// This is the new way to configure HttpSecurity
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // (1)
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll() // (2)
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // (3)
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // (4)

		return http.build();
	}

	// Bean for password encoding
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// This bean is required to authenticate users
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
