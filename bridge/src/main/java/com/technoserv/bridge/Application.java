package com.technoserv.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = { "com.technoserv.bridge" })
public class Application extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run((Object) Application.class, (String[]) args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(new Class[] { Application.class });
	}
}