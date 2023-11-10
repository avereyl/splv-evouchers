package org.splv.evouchers.api.config;

import org.splv.evouchers.api.config.metadata.MetadataProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetadataConfig {

	@Bean
	MetadataProperties metadataProperties() {
		return new MetadataProperties();
	}
}
