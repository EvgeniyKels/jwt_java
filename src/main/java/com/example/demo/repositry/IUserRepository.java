package com.example.demo.repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.User;

public interface IUserRepository extends JpaRepository<User, Long>{
	User findByUsername(String name);
}
