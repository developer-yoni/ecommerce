package com.example.ecommerce;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@Slf4j
public class ECommerceApplication {

	@PostConstruct
	public void printTimeZone() {

		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		log.info(" *** System TimeZone = {}", TimeZone.getDefault());
	}

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

}
