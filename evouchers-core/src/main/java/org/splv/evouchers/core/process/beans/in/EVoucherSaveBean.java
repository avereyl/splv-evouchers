package org.splv.evouchers.core.process.beans.in;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.domain.EVoucherPaymentMethod;
import org.splv.evouchers.core.process.beans.validation.DonorAddressConstraint;
import org.splv.evouchers.core.process.beans.validation.DonorNameConstraint;
import org.splv.evouchers.core.process.beans.validation.MultilineConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DonorNameConstraint
@DonorAddressConstraint
public class EVoucherSaveBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	

	@NotNull
	private EVoucherDonorType donorType;

	@MultilineConstraint(maxLines = Constants.DONOR_NAME_LINES_MAX_NB, maxLengthPerLine = Constants.DONOR_NAME_LINES_MAX_LENGTH)
	private String donorName;
	
	@Size(max = Constants.DONOR_NAME_LINES_MAX_LENGTH)
	private String donorLastname;
	
	@Size(max = Constants.DONOR_NAME_LINES_MAX_LENGTH)
	private String donorFirstname;
	
	private Long version = 0L;

	/**
	 * Address is checked at bean level @see {@link DonorAddressConstraint}
	 * Address may consist of several lines.
	 */
	@MultilineConstraint(maxLines = Constants.DONOR_ADDRESS_LINES_MAX_NB, maxLengthPerLine = Constants.DONOR_ADDRESS_LINES_MAX_LENGTH)
	private String donorAddress;
	
	@Size(max = Constants.DONOR_ADDRESS_ZIPCODE_MAX_LENGTH)
	private String donorZipcode;
	
	@Size(max = Constants.DONOR_ADDRESS_CITY_MAX_LENGTH)
	private String donorCity;
	
	@NotNull
	@Email
	private String donorEmail;
	@NotNull
	@Positive
	private BigDecimal amount;
	@NotNull
	private ZonedDateTime donationDate;
	@NotNull
	private EVoucherPaymentMethod paymentMethod;
	@NotNull
	@Positive
	private Integer distributionYear;

	// only used to help to identify the template origin for new bean / not used for creation or save
	private Integer originalDistributionYear;
	
	private Double donorLatitude;
	private Double donorLongitude;

}
