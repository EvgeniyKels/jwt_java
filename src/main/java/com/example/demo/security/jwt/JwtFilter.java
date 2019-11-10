package com.example.demo.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

/**
 * filter that resolves token
 * and then validate it
 * @author user
 *
 */
public class JwtFilter extends GenericFilterBean {
	
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	public JwtFilter (JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
//		boolean validateToken = jwtTokenProvider.validateToken(token);
		if(token != null) {
			Jws<Claims> validateToken = null;
			try {
				validateToken = jwtTokenProvider.validateToken(token);
			} catch (JwtException e) {
				System.out.println(e.getClass().getName() + "hui");
				PrintWriter writer = response.getWriter();
				writer.write("Jwt call");
			}
			
			if (validateToken != null) {
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				if(authentication != null) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		
		chain.doFilter(request, response);
	}

}
