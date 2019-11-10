package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.security.JWTUserDetailsService;
import com.example.demo.security.jwt.JwtConfigurer;
import com.example.demo.security.jwt.JwtTokenProvider;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private JWTUserDetailsService userDetailsService;
	
	private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
	private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
	private static final String REFRESH_ENDPOINT = "/api/v1/auth/refresh";

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //dont create session for any user
			.and()
			.authorizeRequests()
			.antMatchers(LOGIN_ENDPOINT).permitAll()
			.antMatchers(REFRESH_ENDPOINT).permitAll()
			.antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
			.anyRequest().authenticated()
			.and()
			.apply(new JwtConfigurer(jwtTokenProvider));
	}
}