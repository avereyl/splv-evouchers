package org.splv.evouchers.core.process.beans.converter;

import java.time.ZonedDateTime;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucherEvent;
import org.splv.evouchers.core.process.beans.out.EVoucherEventObject;
import org.springframework.core.convert.converter.Converter;

public class EVoucherEventObjectConverter implements Converter<EVoucherEvent, EVoucherEventObject> {

	@Override
	public EVoucherEventObject convert(EVoucherEvent source) {
		return new EVoucherEventObject(source.getType(), source.getCreatedBy().orElse(Constants.DEFAULT_AUDITOR),
				source.getCreatedDate().orElse(ZonedDateTime.now()));
	}

}
