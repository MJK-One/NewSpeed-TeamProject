package com.newspeed.newspeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NewspeedApplication {
	public static void main(String[] args) {
		SpringApplication.run(NewspeedApplication.class, args);
	}

}
