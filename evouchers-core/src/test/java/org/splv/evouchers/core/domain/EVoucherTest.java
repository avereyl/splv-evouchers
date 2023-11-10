package org.splv.evouchers.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EVoucherTest {

	@ParameterizedTest
	@MethodSource("providesEVouchers")
	void eVoucherReferenceTest(EVoucher eVoucher) {
		System.out.println(EVoucher.getFormattedReference(eVoucher));
		assertEquals(eVoucher.getId(), EVoucher.getIdFromReference(EVoucher.getFormattedReference(eVoucher)).get());
	}
	
	private static Stream<Arguments> providesEVouchers() {
		Arguments[] eVouchers = LongStream.range(1, 30).mapToObj(i -> {
			EVoucher eVoucher = new EVoucher();
			eVoucher.setDistributionYear(2022);
			eVoucher.setId(i);
			return eVoucher;
		}).map(Arguments::of).toList().toArray(new Arguments[0]);
		return Stream.of(eVouchers);
	}
}
