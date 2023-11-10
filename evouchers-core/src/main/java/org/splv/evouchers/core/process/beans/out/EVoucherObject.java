package org.splv.evouchers.core.process.beans.out;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.domain.EVoucherPaymentMethod;
import org.splv.evouchers.core.domain.EVoucherStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EVoucherObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	private long id;
	private long version;
	private String reference;
	private ZonedDateTime createdDate;
	private String createdBy;
	private ZonedDateTime lastModifiedDate;
	private String lastModifiedBy;
	
	private EVoucherDonorType donorType;
	private String donorName;
	private String donorLastname;
	private String donorFirstname;
	private String donorAddress;
	private String donorZipcode;
	private String donorCity;
	private String donorEmail;
	
	private Double donorLatitude;
	private Double donorLongitude;
	
	private BigDecimal amount;
	private ZonedDateTime donationDate;
	private EVoucherPaymentMethod paymentMethod;
	private EVoucherStatus status;
	private Integer distributionYear;
	private Integer metadataVersion;
	private List<EVoucherEventObject> events = new ArrayList<>();
	
	private List<EVoucherDocumentMetadataObject> documents = new ArrayList<>();

}
