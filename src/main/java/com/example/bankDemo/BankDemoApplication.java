package com.example.bankDemo;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "BankDemo application",
				description = "It simulates the banking processes which include credit, debit and transfer",
				version = "v1.0",
				contact = @Contact(name = "Nyophi",
									email = "philipnyoro4@gmail.com",
									url = "https://github.com/prawww"
		)),
		externalDocs = @ExternalDocumentation(
				description = "Banking application",
				url = "https://github.com/prawww"
		)
)
public class BankDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankDemoApplication.class, args);
	}

}
