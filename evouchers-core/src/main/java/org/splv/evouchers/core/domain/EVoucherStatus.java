package org.splv.evouchers.core.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EVoucherStatus {

	UNKNOWN(0), IN_PROGRESS(1), SENT(2), UPDATED(3), ARCHIVED(4);
	
//	  ? -> unknown
//	  0 -> in_progress (default status after creation)
//	  in_progress -> sent
//	  sent -> updated
//	  updated -> sent
//	  sent -> archived
//	  in_progress -> x

	private static final Map<Integer, EVoucherStatus> ENUM_MAP = new HashMap<>();
	static {
		Arrays.asList(EVoucherStatus.values()).forEach(status -> ENUM_MAP.put(status.getValue(), status));
	}

	public static EVoucherStatus fromValue(Integer value) {
		return ENUM_MAP.getOrDefault(value, UNKNOWN);
	}

	private final Integer value;

	
}
