package org.splv.evouchers.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties(prefix = "app.mailing")
public class MailingProperties {

	String from;
	String reply = "";
	String archive;
	
	public String getReply() {
		return StringUtils.hasLength(reply) ? reply : from;
	}

	
}
