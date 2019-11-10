package com.example.demo.security.jwt;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.models.Role;
import com.example.demo.models.Status;
import com.example.demo.models.User;


public final class JwtUserFactory {
	
	public static JwtUser create (User user) {
		return new JwtUser(
				user.getId(),
				user.getUsername(),
				user.getFirstName(),
				user.getLastName(),
				user.getPassword(),
				user.getEmail(),
				user.getStatus().equals(Status.ACTIVE),
				user.getUpdated(),
				mapToGrantedAuthority(user.getRoles())
				);
	}
	
	private static List<GrantedAuthority> mapToGrantedAuthority (List<Role> userRole) {
		return userRole.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());	
	}
}