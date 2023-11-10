package org.splv.evouchers.core.process.beans.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;

public class DonorAddressValidator implements ConstraintValidator<DonorAddressConstraint, EVoucherSaveBean> {

	@Override
	public boolean isValid(EVoucherSaveBean value, ConstraintValidatorContext context) {
		return (value.getDonorType() == EVoucherDonorType.INDIVIDUAL)
				|| StringUtils.isNoneBlank(value.getDonorAddress(), value.getDonorZipcode(), value.getDonorCity());
	}

}
