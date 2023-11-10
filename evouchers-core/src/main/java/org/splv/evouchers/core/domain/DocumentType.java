package org.splv.evouchers.core.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentType {

	VOUCHER(1, "document.type.voucher"), // persisted
	CERFA(2, "document.type.cerfa"), // persisted
	UNKNOWN(0, "document.type.unknown");// transient

	private static final Map<Integer, DocumentType> ENUM_MAP = new HashMap<>();
	static {
		Arrays.asList(DocumentType.values()).forEach(type -> ENUM_MAP.put(type.getValue(), type));
	}

	public static DocumentType fromValue(Integer value) {
		return ENUM_MAP.getOrDefault(value, UNKNOWN);
	}

	private final Integer value;
	private final String i18nKey;

}
