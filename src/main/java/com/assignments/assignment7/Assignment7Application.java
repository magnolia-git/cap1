package com.assignments.assignment7;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Assignment7Application {

	public static void main(String[] args) {
		SpringApplication.run(Assignment7Application.class, args);
		//CheckingAccount checkingAccount = 
	}

	@Bean
	public Docket swaggerConfiguration() {

		return new Docket(DocumentationType.SWAGGER_2)
				.select().paths(PathSelectors.ant("/api/**"))
				.apis(RequestHandlerSelectors.basePackage("com.assignments.assignment7"))
				.build()
				.apiInfo(apiDetails())
				.securitySchemes(Arrays.asList(apiKey()));
	
	}
	
	private ApiKey apiKey() {
		// TODO Auto-generated method stub
		return new ApiKey("jwtToken", "Authorization", "header");
	}

	private ApiInfo apiDetails() {
		return new ApiInfo(
				"Merit Bank App", 
				"API for Merit Bank Application", 
				"1.0", 
				"Free to use", 
				new springfox.documentation.service.Contact("Team 9", "meritbank.com", "team9@meritbak.com"),
				"support@MeritBank.com", 
				"Merit Bank", Collections.emptyList());
				
	}
}
