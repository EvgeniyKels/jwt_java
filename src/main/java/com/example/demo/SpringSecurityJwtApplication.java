package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.models.Role;
import com.example.demo.models.Status;
import com.example.demo.models.User;
import com.example.demo.repositry.IRoleRepository;
import com.example.demo.service.IUserService;

@SpringBootApplication
public class SpringSecurityJwtApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(SpringSecurityJwtApplication.class, args);
		IUserService dbService = run.getBean(IUserService.class);
		IRoleRepository roleRepo = run.getBean(IRoleRepository.class);
		rolesInserting(roleRepo);
		User user = userCreation();	
		User userUser = userUserCreation();	
		dbService.register(user);
		dbService.register(userUser);
	}

	private static User userUserCreation() {
		User user = new User();
		user.setUsername("USER");
		user.setFirstName("Petya");
		user.setPassword("PASSWORD2");
		user.setEmail("Petya@gmail.com");
		return user;
	}

	private static void rolesInserting(IRoleRepository roleRepo) {
		Role roleAdmin = new Role();
		roleAdmin.setName("ROLE_USER");
		roleAdmin.setStatus(Status.ACTIVE);
		roleRepo.save(roleAdmin);

		Role roleUser = new Role();
		roleUser.setName("ROLE_ADMIN");
		roleUser.setStatus(Status.ACTIVE);
		roleRepo.save(roleUser);
	}

	private static User userCreation() {
		User user = new User();
		user.setUsername("ADMIN");
		user.setFirstName("Vasya");
		user.setPassword("PASSWORD");
		user.setEmail("vasya@gmail.com");
		return user;
	}
}