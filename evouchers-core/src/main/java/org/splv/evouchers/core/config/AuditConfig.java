package org.splv.evouchers.core.config;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class AuditConfig {

	@Bean(name="dateTimeProvider")
	DateTimeProvider dateTimeProvider() {
		return () -> Optional.of(ZonedDateTime.now());
	}

	@Bean
	AuditorAware<String> auditorProvider() {
		return () -> Optional.of("guillaume");
	}
	
}
