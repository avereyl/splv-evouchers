package org.splv.evouchers.api.config.metadata;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "api.metadata")
public class MetadataProperties {

	private String title;
	private String version;
	private String description;
	
	private MetadataContact contact;
}
