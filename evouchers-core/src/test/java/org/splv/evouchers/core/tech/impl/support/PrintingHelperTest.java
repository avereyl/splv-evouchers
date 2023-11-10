package org.splv.evouchers.core.tech.impl.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.EVoucher;
import org.springframework.context.support.StaticMessageSource;

class PrintingHelperTest {
	
	private static StaticMessageSource messageSource;
	
	@BeforeAll
	static void init() {
		messageSource = new StaticMessageSource();
		messageSource.addMessage("currency.euro.decimal", Locale.FRANCE, "cent");
		messageSource.addMessage("currency.euro.decimals", Locale.FRANCE, "cents");
		messageSource.addMessage("currency.dollar.decimal", Locale.US, "cent");
		messageSource.addMessage("currency.dollar.decimals", Locale.US, "cents");
	}

	@Test
	void formatAmountTest() {
		assertEquals("10,50 €", PrintingHelper.formatAmount(10.50f, Locale.FRANCE));
		assertEquals("$10.50", PrintingHelper.formatAmount(10.50f, Locale.US));
	}
	@Test
	void formatAmountLitteralTest() {
		assertEquals("dix euros cinquante", PrintingHelper.formatAmountLitteral(10.50f, Locale.FRANCE, messageSource));
		assertEquals("ten US dollars fifty", PrintingHelper.formatAmountLitteral(10.50f, Locale.US, messageSource));
	}
	
	@Test
	void formatAmountNoCurrencyTest() {
		assertEquals("10,50", PrintingHelper.formatAmountNoCurrency(10.50f, Locale.FRANCE));
		assertEquals("10.50", PrintingHelper.formatAmountNoCurrency(10.50f, Locale.US));
	}
	
	@Test
	void vouchersFilenamesTest() {
		//given
		EVoucher eVoucher = new EVoucher();
		eVoucher.setId(3L);
		eVoucher.setDistributionYear(2022);
		// when/then
		assertEquals("REF2022000003.pdf", PrintingHelper.computeVoucherFilename(eVoucher));
		assertEquals("REF2022000003_cerfa.pdf", PrintingHelper.computeFiscalDocumentFilename(eVoucher));
	}
}
