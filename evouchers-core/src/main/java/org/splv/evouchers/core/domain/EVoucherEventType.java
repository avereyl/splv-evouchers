package org.splv.evouchers.core.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EVoucherEventType {

	ACK(1), // transient
	CREATION(2), // persisted
	UPDATE(3), // persisted
	PRINTING(4), // persisted
	DISPATCH(5), // persisted
	ARCHIVING(6), // persisted
	RESTORING(7), // persisted
	NO_OPERATION(8), // transient
	ERROR(9), // transient
	UNKNOWN(0);// transient

	private static final Map<Integer, EVoucherEventType> ENUM_MAP = new HashMap<>();
	static {
		Arrays.asList(EVoucherEventType.values()).forEach(type -> ENUM_MAP.put(type.getValue(), type));
	}

	public static EVoucherEventType fromValue(Integer value) {
		return ENUM_MAP.getOrDefault(value, UNKNOWN);
	}

	private final Integer value;

}
