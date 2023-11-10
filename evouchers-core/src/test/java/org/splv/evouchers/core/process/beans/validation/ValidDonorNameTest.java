package org.splv.evouchers.core.process.beans.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.domain.EVoucherPaymentMethod;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;

class ValidDonorNameTest {

	private static Validator validator;
	
	@BeforeAll
	static void init() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Test
	void whenIndividualHasLastNameAndFirstname_thenSuccess() {
		// given
		EVoucherSaveBean bean = getBasicEVoucherSaveBean();
		bean.setDonorType(EVoucherDonorType.INDIVIDUAL);
		bean.setDonorLastname("Doe");
		bean.setDonorFirstname("John");
		// when
		Set<ConstraintViolation<EVoucherSaveBean>> violations = validator.validate(bean);
		// then
		assertTrue(violations.isEmpty());
	}
	
	@Test
	void whenIndividualMissLastNameOrFirstname_thenFailure() {
		// given
		EVoucherSaveBean bean1 = getBasicEVoucherSaveBean();
		EVoucherSaveBean bean2 = getBasicEVoucherSaveBean();
		bean1.setDonorType(EVoucherDonorType.INDIVIDUAL);
		bean2.setDonorType(EVoucherDonorType.INDIVIDUAL);
		
		bean1.setDonorLastname("Doe");
		bean2.setDonorFirstname("John");
		// when
		Set<ConstraintViolation<EVoucherSaveBean>> violations1 = validator.validate(bean1);
		Set<ConstraintViolation<EVoucherSaveBean>> violations2 = validator.validate(bean2);
		// then
		assertFalse(violations1.isEmpty());
		assertFalse(violations2.isEmpty());
	}
	
	@Test
	void whenProfessionalHasName_thenSuccess() {
		// given
		EVoucherSaveBean bean = getBasicEVoucherSaveBean();
		bean.setDonorType(EVoucherDonorType.PROFESSIONAL);
		bean.setDonorName("Acme company");
		// when
		Set<ConstraintViolation<EVoucherSaveBean>> violations = validator.validate(bean);
		// then
		assertTrue(violations.isEmpty());
	}
	
	@Test
	void whenProfessionalMissName_thenFailure() {
		// given
		EVoucherSaveBean bean = getBasicEVoucherSaveBean();
		bean.setDonorType(EVoucherDonorType.PROFESSIONAL);
		// when
		Set<ConstraintViolation<EVoucherSaveBean>> violations = validator.validate(bean);
		// then
		assertFalse(violations.isEmpty());
	}
	@Test
	void whenProfessionalWithEmptyName_thenFailure() {
		// given
		EVoucherSaveBean bean = getBasicEVoucherSaveBean();
		bean.setDonorType(EVoucherDonorType.PROFESSIONAL);
		bean.setDonorName(" ");
		// when
		Set<ConstraintViolation<EVoucherSaveBean>> violations = validator.validate(bean);
		// then
		assertFalse(violations.isEmpty());
	}
	
	@Test
	void whenUnknown_thenFailure() {
		// given
		EVoucherSaveBean bean = getBasicEVoucherSaveBean();
		bean.setDonorType(EVoucherDonorType.UNKNOWN);
		bean.setDonorLastname("Doe");
		bean.setDonorFirstname("John");
		bean.setDonorName("Acme company");
		// when
		Set<ConstraintViolation<EVoucherSaveBean>> violations = validator.validate(bean);
		// then
		assertFalse(violations.isEmpty());
	}

	
	private EVoucherSaveBean getBasicEVoucherSaveBean() {
		var bean = new EVoucherSaveBean();
		bean.setDonorEmail("john.doe@acme.com");
		bean.setDonorAddress("Rue de l'adresse");
		bean.setDonorZipcode("85130");
		bean.setDonorCity("CHANVERRIE");
		bean.setDistributionYear(2022);
		bean.setPaymentMethod(EVoucherPaymentMethod.CASH);
		bean.setDonationDate(ZonedDateTime.now());
		bean.setAmount(BigDecimal.TEN);
		return bean;
	}
}
