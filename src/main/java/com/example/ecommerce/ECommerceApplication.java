package com.example.ecommerce;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ECommerceApplication {

	@Value("${spring.datasource.url}")
	private String url;

	@PostConstruct
	public void printTimeZone() {

		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		System.out.println("***** SystemTimeZone : "+ TimeZone.getDefault() + " *********" );
	}

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

}
