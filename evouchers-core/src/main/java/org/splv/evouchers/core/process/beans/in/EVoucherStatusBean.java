package org.splv.evouchers.core.process.beans.in;

import java.io.Serializable;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucherStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EVoucherStatusBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	private EVoucherStatus status;

}
