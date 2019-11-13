package com.example.demo.security.jwt;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.example.demo.models.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	@Value("${jwt.token.secret}")
	private String secret;
	@Value("${jwt.token.expired}")
	private long validityInMillis;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@PostConstruct
	protected void init() {
		secret = Base64.getEncoder().encodeToString(secret.getBytes());
	}
	/**
	 * creating token
	 * @param userName
	 * @param collection
	 * @return
	 */
	public String createToken(String userName, Collection<? extends GrantedAuthority> collection) {
		Map<String, Object> customClaims = new HashMap<>();
		customClaims.put("claim", "hieem");
		customClaims.put("claim2", "hieem2");
		Claims claims = Jwts.claims(customClaims).setSubject(userName).setIssuer("kels");
		
		claims.put("roles", getRoleNames(collection));
		
		Date now = new Date();
		Date validityDate = new Date(now.getTime() + validityInMillis);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(validityDate)
				.setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	public String createRefreshToken(String userName) {
		Map<String, Object> customClaims = new HashMap<>();
		customClaims.put("scope", "refresh");
		Claims claims = Jwts.claims(customClaims).setSubject(userName).setIssuer("kels");
		Date now = new Date();
		Date validityDate = new Date(now.getTime() + validityInMillis * 1000);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(validityDate)
				.setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	
	public Authentication getAuthentication(String token) {
		String userName = getUserName(token);
//		UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
//		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
		return new UsernamePasswordAuthenticationToken(userName,null, new ArrayList<>());
	}
	
	public String getUserName(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Autorization");
		if(bearerToken != null && bearerToken.startsWith("Bearer_")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
	
	/**
	 * Claims - is library
	 * @param token
	 * @return
	 */
	public Jws<Claims> validateToken(String token) throws JwtException {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			if(claims.getBody().getExpiration().before(new Date())) {
				return null;
			}
			return claims;
		} catch (IllegalArgumentException e) {
			throw new JwtAuthentiphicationException("Jwt token invalid");
		}
	}
	
	private List<String> getRoleNames(Collection<? extends GrantedAuthority> collection) {
		return collection.stream()
				.map(x -> x.getAuthority())
				.collect(Collectors.toList());
//				map(role -> role.getName())
//				.collect(Collectors.toList());
	}
}