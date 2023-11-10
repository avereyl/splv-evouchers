package org.splv.evouchers.core.process.beans.out;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.DocumentType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EVoucherDocumentMetadataObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	private long id;
	private long version;
	
	private ZonedDateTime createdDate;
	private String createdBy;
	private ZonedDateTime lastModifiedDate;
	private String lastModifiedBy;
	
	private DocumentType documentType;
}
