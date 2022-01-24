package com.dvtt.demo.distributedtracinghandmade.animalname;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.dvtt.demo")
public class AnimalNameApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimalNameApplication.class, args);
	}

}
