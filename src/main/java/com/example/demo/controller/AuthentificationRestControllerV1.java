package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthenticationRequestDTO;
import com.example.demo.dto.RefreshRequestDTO;
import com.example.demo.models.User;
import com.example.demo.security.JWTUserDetailsService;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.IUserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthentificationRestControllerV1 {
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private IUserService userService;
	@Autowired
	private JWTUserDetailsService userservice;

	@SuppressWarnings("rawtypes")
	@PostMapping("login")
	public ResponseEntity login(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
		try {
			String userName = authenticationRequestDTO.getUserName();
			String password = authenticationRequestDTO.getPassword();
			authManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
			Map<Object, Object> responseBody = extracted(userName);
			return ResponseEntity.ok(responseBody);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid username or password");
		}
	}
	@PostMapping("refresh")
	public ResponseEntity refresh(@RequestBody RefreshRequestDTO refreshRequestDto) {
		String refreshToken = refreshRequestDto.getRefreshToken();
		
//	TODO	if(refresh not exist)
		
		Jws<Claims> claims = jwtTokenProvider.validateToken(refreshToken);
		Map<Object, Object> responseBody = null;
		if(claims != null) {
			String scope = (String) claims.getBody().get("scope");
			if(scope.equals("refresh")) {
				 responseBody = extracted(claims.getBody().getSubject());
			}
		}
		return ResponseEntity.ok(responseBody);
	}
	private Map<Object, Object> extracted(String userName) {
		UserDetails user = userservice.loadUserByUsername(userName);
		if (user == null) {
			throw new UsernameNotFoundException("User with username: " + userName + " notexist in system");
		}
		String token = jwtTokenProvider.createToken(userName, user.getAuthorities());
		String refreshToken = jwtTokenProvider.createRefreshToken(userName);
		Map<Object, Object> responseBody = new HashMap<>();
		responseBody.put("username", userName);
		responseBody.put("token", token);
		responseBody.put("refresh", refreshToken);
		return responseBody;
	}
}