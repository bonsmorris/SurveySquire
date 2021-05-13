package com.claim.springbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@ComponentScan(basePackages="com.claim")
public class SpringBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBackEndApplication.class, args);
	}

}
