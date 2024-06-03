package com.platform.platformOffreM;

import com.platform.platformOffreM.entities.Admin;
import com.platform.platformOffreM.entities.Role;
import com.platform.platformOffreM.services.RoleService;
import com.platform.platformOffreM.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Stream;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)

public class PlatformOffreDeMApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformOffreDeMApplication.class, args);

	}


	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}



	@Bean
	CommandLineRunner start(RoleService roleService, UserService userService) {
		return args -> {
			Stream.of("ADMIN", "TALENT", "ENTREPRISE")
					.forEach(roleName -> {
						Role role = new Role();
						role.setRoleName(roleName);
						roleService.addRole(role);
					});
			Admin admin = new Admin();
			admin.setNom("admin");
			admin.setUsername("admin");
			admin.setPassword("admin");
			admin.setEmail("yessinhadded9@gmail.com");
			admin.setRole(roleService.findRoleByRoleName("ADMIN"));
			userService.addUser(admin);

		};
	}

}