package com.example.ecommerce.global.response.validation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.ObjectError;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ValidationFailForObject {
	
	private String objectName;
	private String defaultMessage;
	
	public static ValidationFailForObject convert (ObjectError objectError){

		return ValidationFailForObject.builder()
									  .objectName(objectError.getObjectName())
									  .defaultMessage(objectError.getDefaultMessage())
									  .build();
	}
}
