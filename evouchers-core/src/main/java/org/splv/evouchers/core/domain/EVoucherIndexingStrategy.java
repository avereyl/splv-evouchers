package org.splv.evouchers.core.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EVoucherIndexingStrategy {

	/**
	 * The e-voucher is indexed if status is active or archived
	 * (according roles).
	 */
	INDEX(1),
	/**
	 * The e-voucher is not indexed.
	 */
	IGNORE(0);

	private static final Map<Integer, EVoucherIndexingStrategy> ENUM_MAP = new HashMap<>();
	static {
		Arrays.asList(EVoucherIndexingStrategy.values()).forEach(method -> ENUM_MAP.put(method.getValue(), method));
	}

	public static EVoucherIndexingStrategy fromValue(Integer value) {
		return ENUM_MAP.getOrDefault(value, INDEX);
	}

	private final Integer value;
}
