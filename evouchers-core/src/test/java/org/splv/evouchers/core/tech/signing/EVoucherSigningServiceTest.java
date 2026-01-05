package org.splv.evouchers.core.tech.signing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.domain.EVoucherPaymentMethod;
import org.splv.evouchers.core.tech.EVoucherSigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@SpringBootTest(properties = { "app.signing.keystore.path=classpath:/keystores/dummy-keystore.p12",
		"app.signing.keystore.password=changeit", "app.signing.keystore.default-kid=default" })
class EVoucherSigningServiceTest {

	@Configuration
	@EnableAutoConfiguration
	@ComponentScan(basePackageClasses = {
			EVoucherSigningService.class },
		useDefaultFilters = false, 
		includeFilters = {
				@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*EVoucherSigningService.*"), 
				@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*CommonTestConfiguration.*")
				})
	static class SigningServiceTestConfiguration {
	}

	@Autowired
	private EVoucherSigningService signingService;

	@Test
	void whenSigningEVoucher_thenVerifySignatureIsOK() throws IOException {
		assertNotNull(signingService);
		String signature = signingService.signEVoucher(defaultEVoucher());
		boolean result = signingService.verifyEVoucherSignature(signature);		
		assertTrue(result);
	}
	
	@ParameterizedTest()
	@ValueSource(ints = {10, 15, 20, 0})
	void whenGeneratingRandomSizeIsRespected(int n) {
		var randomValue = EVoucherSigningService.randomAlphanumeric(n);
		assertEquals(n, randomValue.length());
		assertTrue(StringUtils.isEmpty(randomValue) || StringUtils.isAlphanumeric(randomValue));
	}

	private EVoucher defaultEVoucher() {
		// given
		var eVoucher = new EVoucher();
		eVoucher.setId(01L);
		eVoucher.setDistributionYear(2024);
		eVoucher.setAmount(new BigDecimal(10));
		eVoucher.setDonorType(EVoucherDonorType.PROFESSIONAL);
		eVoucher.setPaymentMethod(EVoucherPaymentMethod.CHECK);
		eVoucher.setDonationDate(ZonedDateTime.now());

		eVoucher.setDonorName("John Doe\nJunior");
		eVoucher.setDonorAddress("Acme Road\nHollywood Bd\n...");
		eVoucher.setDonorCity("LA");
		eVoucher.setDonorZipcode("00000");
		return eVoucher;
	}
	
	

}
