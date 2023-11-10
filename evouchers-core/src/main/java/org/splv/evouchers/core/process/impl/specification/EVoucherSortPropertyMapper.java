package org.splv.evouchers.core.process.impl.specification;

import java.util.HashMap;
import java.util.Map;

import org.splv.evouchers.core.domain.EVoucher_;
import org.springframework.data.domain.Sort.Order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EVoucherSortPropertyMapper {

	/**
	 * Map DTO object field to domain object fields for sorting.
	 */
	private static final Map<String, String> PROPERTY_PATH_MAP = new HashMap<>();
	static {
		PROPERTY_PATH_MAP.put("donationDate", EVoucher_.DONATION_DATE);
		PROPERTY_PATH_MAP.put("paymentMethod", EVoucher_.PAYMENT_METHOD);
	}
	
	public static boolean isSortOrderSupported(Order o) {
		return PROPERTY_PATH_MAP.containsKey(o.getProperty());
	}
	
	public static String mapProperty(String property) {
		return PROPERTY_PATH_MAP.get(property);
	}
	
	public static Order map(Order o) {
		String mappedProperty = EVoucherSortPropertyMapper.mapProperty(o.getProperty());
		return o.isAscending() ? Order.asc(mappedProperty) : Order.desc(mappedProperty);
	}
	
}
