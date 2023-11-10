package org.splv.evouchers.api.error;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.splv.evouchers.api.Constants;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultAPIResponseEntityExceptionHandler {

	private final MessageSource messageSource;
	private final LocaleResolver localeResolver;
	
	private Locale getLocale(HttpServletRequest request) {
		return localeResolver.resolveLocale(request);
	}
	
	private Locale getLocale(WebRequest request) {
		var servletWebRequest = (ServletWebRequest) request;
		return getLocale(servletWebRequest.getRequest());
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({Exception.class})
	public ResponseEntity<APIError> handleException(Exception ex, WebRequest request) {
		log.error("Unexpected exception", ex);
		var headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(Constants.DEFAULT_API_CONTENT_TYPE_V1));
		var key = "errors.api.common.unexpected";
		String message = messageSource.getMessage(key, null, getLocale(request));
		var apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR, key, message);
		return new ResponseEntity<>(apiError, headers, apiError.getStatus());
	}
}
