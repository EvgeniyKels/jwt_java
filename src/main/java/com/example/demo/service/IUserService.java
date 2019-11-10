package com.example.demo.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.example.demo.models.User;

public interface IUserService {
	User register(User user);
	List<User>getAll();
	User findByUsername(String userName);
	JSONObject findById(Long id);
	void deleteUser(Long id);
}
