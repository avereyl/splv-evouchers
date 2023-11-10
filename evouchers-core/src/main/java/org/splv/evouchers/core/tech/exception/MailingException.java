package org.splv.evouchers.core.tech.exception;

import org.splv.evouchers.core.Constants;

@SuppressWarnings("java:S2166") // extending RuntimeException !!!
public class MailingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;

	public MailingException() {
	}

	public MailingException(String message) {
		super(message);
	}

	public MailingException(Throwable cause) {
		super(cause);
	}

	public MailingException(String message, Throwable cause) {
		super(message, cause);
	}

}
