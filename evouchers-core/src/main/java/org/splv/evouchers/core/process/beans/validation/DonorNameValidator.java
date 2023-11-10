package org.splv.evouchers.core.process.beans.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;

public class DonorNameValidator implements ConstraintValidator<DonorNameConstraint, EVoucherSaveBean> {

	@Override
	public boolean isValid(EVoucherSaveBean value, ConstraintValidatorContext context) {
		return (value.getDonorType() == EVoucherDonorType.PROFESSIONAL && !isNullOrBlank(value.getDonorName()))
				|| (value.getDonorType() == EVoucherDonorType.INDIVIDUAL && !isNullOrBlank(value.getDonorFirstname())
						&& !isNullOrBlank(value.getDonorLastname()));
	}

	private boolean isNullOrBlank(final String s) {
		return s == null || s.isBlank();
	}

}
