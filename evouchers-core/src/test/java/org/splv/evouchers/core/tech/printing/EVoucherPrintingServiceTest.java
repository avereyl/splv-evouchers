package org.splv.evouchers.core.tech.printing;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.config.properties.MailingProperties;
import org.splv.evouchers.core.config.properties.VoucherGenerationProperties;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.domain.EVoucherPaymentMethod;
import org.splv.evouchers.core.tech.EVoucherPrintingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(properties = { "app.signing.keystore.path=classpath:/keystores/dummy-keystore.p12",
		"app.signing.keystore.password=changeit", "app.signing.keystore.default-kid=default",
		"app.mailing.from=test@splv.fr" })
class EVoucherPrintingServiceTest {

	@Configuration
	@EnableAutoConfiguration
	@ComponentScan(basePackageClasses = { EVoucherPrintingService.class })
	static class PrintingServiceTestConfiguration {

		@Bean
		JavaMailSender javaMailSender() {
			return new JavaMailSenderImpl();
		}

		@Bean
		freemarker.template.Configuration freemarkerConfiguration() {
			return new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30);
		}

		@Bean
		MailingProperties mailingProperties() {
			var mailingProperties = new MailingProperties();
			return mailingProperties;
		}

		@Bean
		VoucherGenerationProperties voucherGenerationProperties() {
			var voucherGenerationProperties = new VoucherGenerationProperties();
			var voucherProperties = new VoucherGenerationProperties.VoucherProperties();
			var voucherResponsibleProperties = new VoucherGenerationProperties.VoucherResponsibleProperties();
			voucherResponsibleProperties.setName("John Doe");
			voucherResponsibleProperties.setSignaturePath("classpath:assets/sgn/signature-mock.svg");
			voucherProperties.setResponsible(voucherResponsibleProperties);
			voucherGenerationProperties.setProperties(Map.of(0, voucherProperties));
			return voucherGenerationProperties;
		}

		@Primary
		@Bean("coreMessageSource")
		MessageSource messageSource() {
			ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
			messageSource.setBasename("classpath:i18n/core-messages");
			messageSource.setDefaultEncoding("UTF-8");
			return messageSource;
		}
	}

	@Autowired
	private EVoucherPrintingService printingService;

	@Autowired
	private Environment environment;

	@Test
	void whenMaximumOfLines_thenDocumentIsPrinted() throws IOException {
		assertNotNull(printingService);
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

		String fileName = System.getProperty("java.io.tmpdir").concat(UUID.randomUUID().toString()).concat(".pdf");
		// when
		try (ByteArrayOutputStream baos = printingService.printEVoucher(eVoucher, Locale.FRANCE);
				FileOutputStream outputStream = new FileOutputStream(fileName);) {

			boolean notCIBuild = Stream.of(environment.getActiveProfiles()).noneMatch(s -> "ci".equalsIgnoreCase(s));

			if (notCIBuild) {
				// then
				baos.writeTo(outputStream);
				// path of the file for visual check
				log.info(fileName);
			} else {
				log.info("No file generated - ci profile detected.");
			}
		}

	}

}
