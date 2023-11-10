package org.splv.evouchers.core.tech.exception;

import org.splv.evouchers.core.Constants;

import lombok.Getter;

public class TemplatingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;

	@Getter
	private final String templateName;

	public TemplatingException(String templateName, String message) {
		super(message);
		this.templateName = templateName;
	}

	public TemplatingException(String templateName, Throwable cause) {
		super(cause);
		this.templateName = templateName;
	}

	public TemplatingException(String templateName, String message, Throwable cause) {
		super(message, cause);
		this.templateName = templateName;
	}

}
