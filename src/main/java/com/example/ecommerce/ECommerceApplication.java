package com.example.ecommerce;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ECommerceApplication {

	@PostConstruct
	public void setTimezone() {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("***** Springboot Timezone : " + TimeZone.getDefault());
	}

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}
}
