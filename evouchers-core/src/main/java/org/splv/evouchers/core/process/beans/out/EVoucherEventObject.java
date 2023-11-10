package org.splv.evouchers.core.process.beans.out;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucherEventType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EVoucherEventObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;

	private EVoucherEventType eventType;
	private String createdBy;
	private ZonedDateTime createdDate;
	
	public static EVoucherEventObject buildEvent(EVoucherEventType eventType, String auditor) {
		return new EVoucherEventObject(eventType, auditor, ZonedDateTime.now());
	}
	public static EVoucherEventObject buildEvent(EVoucherEventType eventType) {
		return EVoucherEventObject.buildEvent(eventType, Constants.DEFAULT_AUDITOR);
	}

	
}
