package com.dvtt.demo.distributedtracinghandmade.scientistname;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.dvtt.demo")
public class ScientistNameApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScientistNameApplication.class, args);
	}

}
