package com.filestone.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * Spring-Boot java module Initializer Class
 * @author Hoffman
 *
 */

//Declaring a Spring-Boot application Allow us to get Auto-Configuration and Auto-Component-Scanning and more for this java-module
@SpringBootApplication
//Declaring where the Spring-IOC-Container will be scanning for Spring-Beans
@ComponentScan("com.filestone")
//Declaring where the Spring-IOC-Container will be scanning for DB-Entities to be used by JPA/Hibernate ORM System
@EntityScan("com.filestone")
//Declaring where the Spring-IOC-Container will be scanning for the DAO-Persistence layer to be used by JPA-Spring Data
@EnableJpaRepositories({ "com.filestone" })
public class FilestoneApplication extends SpringBootServletInitializer{

	/**
	 * Spring-Boot Main class
	 * 
	 * @param args
	 */
	
	
	 @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
	    return builder.sources(FilestoneApplication.class);
	  }
	public static void main(String[] args) {
		SpringApplication.run(FilestoneApplication.class, args);
	}
}
