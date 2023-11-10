package org.splv.evouchers.core.process.beans.in;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.splv.evouchers.core.Constants;

import lombok.Data;

@Data
public class EVoucherFilterBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;
}
