package com.example.demo.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthentiphicationException extends AuthenticationException {
	
	private static final long serialVersionUID = 4966378278592194834L;
	
	public JwtAuthentiphicationException(String msg) {
		super(msg);
	}
	
	public JwtAuthentiphicationException(String msg, Throwable t) {
		super(msg, t);
	}
}