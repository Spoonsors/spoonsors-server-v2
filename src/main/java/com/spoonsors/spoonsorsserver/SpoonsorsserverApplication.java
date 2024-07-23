package com.spoonsors.spoonsorsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpoonsorsserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpoonsorsserverApplication.class, args);
	}

}
