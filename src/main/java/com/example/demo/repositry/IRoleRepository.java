package com.example.demo.repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Role;

public interface IRoleRepository extends JpaRepository<Role, String> {
	Role findByName(String name);
}
