package org.splv.evouchers.api.config;

import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.splv.evouchers.core.Constants;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class I18nConfig {

	@Bean
	LocaleResolver localeResolver() {
		return new LocaleResolver() {

			@Override
			public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
				//not used
			}

			@Override
			public Locale resolveLocale(HttpServletRequest request) {
				return Constants.DEFAULT_LOCALE;
			}
		};
	}
	
	@Primary
	@Bean
	MessageSource messageSource() {
		var messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:i18n/core-messages", "classpath:i18n/api-messages");
		messageSource.setDefaultEncoding(Constants.DEFAULT_ENCODING);
		messageSource.setDefaultLocale(Constants.DEFAULT_LOCALE);
		messageSource.setFallbackToSystemLocale(false);
		return messageSource;
	}

}
