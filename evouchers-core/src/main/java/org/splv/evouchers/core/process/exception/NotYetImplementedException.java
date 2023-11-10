package org.splv.evouchers.core.process.exception;

import org.splv.evouchers.core.Constants;

public class NotYetImplementedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;

	public NotYetImplementedException() {
		super("Please be patient");
	}

	public NotYetImplementedException(String message) {
		super(message);
	}

	public NotYetImplementedException(Throwable cause) {
		super(cause);
	}

	public NotYetImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotYetImplementedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
