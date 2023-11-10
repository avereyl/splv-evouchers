package org.splv.evouchers.core.business.exception;

import org.splv.evouchers.core.Constants;

import lombok.Getter;

@Getter
public class EVoucherNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	/**
	 * Identifier of the eVoucher not found.
	 */
	private final Long id;

	public EVoucherNotFoundException(Long id, String message, Throwable cause) {
		super(message, cause);
		this.id = id;
	}

	public EVoucherNotFoundException(Long id, String message) {
		super(message);
		this.id = id;
	}

}
