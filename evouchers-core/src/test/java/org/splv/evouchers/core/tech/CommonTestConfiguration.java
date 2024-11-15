package org.splv.evouchers.core.tech;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
class CommonTestConfiguration {
	@Primary
	@Bean("coreMessageSource")
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/core-messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
