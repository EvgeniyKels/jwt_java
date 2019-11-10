package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.models.User;
import com.example.demo.security.jwt.JwtUser;
import com.example.demo.security.jwt.JwtUserFactory;
import com.example.demo.service.IUserService;

@Service
public class JWTUserDetailsService implements UserDetailsService {
	@Autowired
	private IUserService userService;
	
	/**
	 * find in my custom user detailservice user by user name and create by it jwt user
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException ("user with username not found");
		}
		JwtUser jwtUser = JwtUserFactory.create(user);
		return jwtUser;
	}

}
