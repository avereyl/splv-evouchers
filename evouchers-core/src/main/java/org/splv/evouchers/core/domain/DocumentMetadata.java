package org.splv.evouchers.core.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.support.AbstractBaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DOCUMENT_METADATA")
@SuppressWarnings("java:S2160") // no need to override equals method here
public class DocumentMetadata extends AbstractBaseEntity {

	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	private DocumentType documentType;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private EVoucher eVoucher;
	
	
}
