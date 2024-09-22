package com.example.especialista.spring.rest;

import com.example.especialista.spring.rest.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class EspecialistaSpringRestApplication {

	public static void main(String[] args) {

		SpringApplication.run(EspecialistaSpringRestApplication.class, args);
	}

}
