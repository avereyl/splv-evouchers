package org.splv.evouchers.api.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

	@Bean
	OpenAPI evouchersAPI() {
// @formatter:off
		return new OpenAPI()
				.info(new Info().title("eVouchers API")
						.description("")
						.version("v1.0")
						.contact(new Contact().name("Guillaume Billaud"))
				);
	}
// @formatter:on

	@Bean
	GroupedOpenApi evouchersAPIv10() {
		// so far only v1.0
		return GroupedOpenApi.builder().group("v1.0").pathsToMatch("/**").build();
	}
	
}
