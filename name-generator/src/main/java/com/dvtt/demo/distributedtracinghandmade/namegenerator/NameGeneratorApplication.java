package com.dvtt.demo.distributedtracinghandmade.namegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.dvtt.demo")
public class NameGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NameGeneratorApplication.class, args);
	}

}
