package org.splv.evouchers.core.process.exception;

import org.splv.evouchers.core.Constants;

import lombok.Getter;

public class EVoucherPrintingException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	/**
	 * EVoucher id.
	 */
	@Getter
	private final long id;

	public EVoucherPrintingException(long id, String message) {
		super(message);
		this.id = id;
	}

	public EVoucherPrintingException(long id, String message, Throwable cause) {
		super(message, cause);
		this.id = id;
	}


}
