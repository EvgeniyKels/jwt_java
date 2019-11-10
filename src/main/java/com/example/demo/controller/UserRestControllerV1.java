package com.example.demo.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.IUserService;

@RestController
@RequestMapping(value = "/api/v1/users/")
public class UserRestControllerV1 {
	@Autowired
	private IUserService userService;
	
	@GetMapping(value = "{id}")
	public ResponseEntity<JSONObject> getUserById(@PathVariable(name = "id") Long id) {
		JSONObject user = userService.findById(id);
		if(user == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(user);
	}
}
