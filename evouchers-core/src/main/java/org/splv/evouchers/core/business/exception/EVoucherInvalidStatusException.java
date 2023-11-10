package org.splv.evouchers.core.business.exception;

import java.util.Set;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucherStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EVoucherInvalidStatusException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	/**
	 * Identifier of the eVoucher not found.
	 */
	private final Long id;

	/**
	 * The status of the eVoucher.
	 */
	private final EVoucherStatus givenStatus;
	/**
	 * Expected statuses.
	 */
	private final Set<EVoucherStatus> expectedStatuses;

}
