package org.splv.evouchers.api.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.splv.evouchers.api.Constants;
import org.splv.evouchers.api.controller.exception.InvalidEVoucherStatusParameterException;
import org.splv.evouchers.core.business.exception.EVoucherInvalidStatusException;
import org.splv.evouchers.core.business.exception.EVoucherNotFoundException;
import org.splv.evouchers.core.business.exception.EVoucherPrintNotFoundException;
import org.splv.evouchers.core.process.exception.NotYetImplementedException;
import org.splv.evouchers.core.tech.exception.MailingException;
import org.splv.evouchers.core.tech.exception.PrintingException;
import org.splv.evouchers.core.tech.exception.QRCodeGenerationException;
import org.splv.evouchers.core.tech.exception.TemplatingException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE-10)
public class APIResponseEntityExceptionHandler extends ResponseEntityExceptionHandler implements InitializingBean {

	private final LocaleResolver localeResolver;
	private final MessageSource messageSource;
	private HttpHeaders defaultHeaders;
	
	private Locale getLocale(HttpServletRequest request) {
		return localeResolver.resolveLocale(request);
	}
	
	private Locale getLocale(WebRequest request) {
		var servletWebRequest = (ServletWebRequest) request;
		return getLocale(servletWebRequest.getRequest());
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		defaultHeaders = new HttpHeaders();
		defaultHeaders.setContentType(MediaType.valueOf(Constants.DEFAULT_API_CONTENT_TYPE_V1));
	}
	
	@ExceptionHandler({ NotYetImplementedException.class })
	public ResponseEntity<APIError> handleNotYetImplementedException(NotYetImplementedException ex,
			WebRequest request) {
		var key = "errors.api.common.not-implemented";
		String message = messageSource.getMessage(key, null, getLocale(request));
		APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR, key, message);
		return new ResponseEntity<>(apiError, defaultHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ EVoucherNotFoundException.class })
	public ResponseEntity<APIError> handleEVoucherNotFoundException(EVoucherNotFoundException ex,
			WebRequest request) {
		var key = "errors.core.business.evoucher-notfound";
		String message = messageSource.getMessage(key, new Object[] { ex.getId() }, getLocale(request));
		var apiError = new APIError(HttpStatus.BAD_REQUEST, key, message);
		return new ResponseEntity<>(apiError, defaultHeaders, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler({ EVoucherPrintNotFoundException.class })
	public ResponseEntity<APIError> handleEVoucherPrintNotFoundException(EVoucherPrintNotFoundException ex,
			WebRequest request) {
		var key = "errors.core.business.evoucherprint-notfound";
		String message = messageSource.getMessage(key, new Object[] { ex.getId() }, getLocale(request));
		var apiError = new APIError(HttpStatus.BAD_REQUEST, key, message);
		return new ResponseEntity<>(apiError, defaultHeaders, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ EVoucherInvalidStatusException.class })
	public ResponseEntity<APIError> handleEVoucherInvalidStatusException(EVoucherInvalidStatusException ex,
			WebRequest request) {
		var key = "errors.core.business.evoucher-invalidstatus";
		String message = messageSource.getMessage(key, new Object[] { ex.getGivenStatus(), ex.getExpectedStatuses() }, getLocale(request));
		var apiError = new APIError(HttpStatus.BAD_REQUEST, key, message);
		return new ResponseEntity<>(apiError, defaultHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler({ InvalidEVoucherStatusParameterException.class })
	public ResponseEntity<APIError> handleInvalidEVoucherStatusParameterException(InvalidEVoucherStatusParameterException ex,
			WebRequest request) {
		var key = "errors.api.business.invalidstatus";
		String message = messageSource.getMessage(key, new Object[] { ex.getGivenStatus(), ex.getExpectedStatuses() },
				getLocale(request));
		var apiError = new APIError(HttpStatus.BAD_REQUEST, key, message);
		return new ResponseEntity<>(apiError, defaultHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	
	

	@ExceptionHandler({ MailingException.class, PrintingException.class, QRCodeGenerationException.class,
			TemplatingException.class })
	public ResponseEntity<APIError> handleCoreTechException(RuntimeException ex, WebRequest request) {
		List<String> errors = new ArrayList<>();
		if (ex instanceof TemplatingException tex) {
			errors.add(String.format("Error rendering text with template %s", tex.getTemplateName()));
		}
		APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR, "", ex.getMessage(), errors);
		return new ResponseEntity<>(apiError, defaultHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
