package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.Role;
import com.example.demo.models.Status;
import com.example.demo.models.User;
import com.example.demo.repositry.IRoleRepository;
import com.example.demo.repositry.IUserRepository;

import lombok.extern.slf4j.Slf4j;
@Service @Slf4j
public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserRepository userRepo;
	@Autowired
	private IRoleRepository roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User register(User user) {
		Role roleUser = roleRepo.findByName("ROLE_USER");
		List<Role> roles = new ArrayList<Role>();
		roles.add(roleUser);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(roles);
		user.setStatus(Status.ACTIVE);
		
		return userRepo.save(user);
	}

	@Override
	public List<User> getAll() {
		return userRepo.findAll();
	}

	@Override
	public User findByUsername(String userName) {
		return userRepo.findByUsername(userName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject findById(Long id) {
		Optional<User> userOpt = userRepo.findById(id);
		if(!userOpt.isPresent()) {
			return null;
		}
		User user = userOpt.get();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", user.getId());
		jsonObject.put("name", user.getUsername());
		jsonObject.put("firstname", user.getFirstName());
		jsonObject.put("lastname", user.getLastName());
		jsonObject.put("email", user.getEmail());
		jsonObject.put("created", user.getCreated());
		JSONArray roles = new JSONArray();
		user.getRoles().stream().map(role -> role.getName()).forEach(roleName -> roles.add(roleName));
		jsonObject.put("role", roles);
		return jsonObject;
	}

	@Override
	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

}
