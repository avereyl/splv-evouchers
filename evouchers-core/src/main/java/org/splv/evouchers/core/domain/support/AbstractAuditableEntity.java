package org.splv.evouchers.core.domain.support;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.splv.evouchers.core.Constants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Setter;

@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("java:S2160") // equals/hashCode handled in AbstractEntity
public abstract class AbstractAuditableEntity extends AbstractEntity implements Auditable<String, Long, ZonedDateTime> {

	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	@CreatedBy
	@Column(nullable=false, updatable = false)
	protected String createdBy;
	
	@CreatedDate
	@Column(nullable=false, updatable = false)
	protected ZonedDateTime createdDate;
	
	@LastModifiedBy
	@Column(nullable=false)
	protected String lastModifiedBy;
	
	@LastModifiedDate
	@Column(nullable=false)
	protected ZonedDateTime lastModifiedDate;

	@Override
	public Optional<String> getCreatedBy() {
		return Optional.ofNullable(this.createdBy);
	}

	@Override
	public Optional<ZonedDateTime> getCreatedDate() {
		return Optional.ofNullable(this.createdDate);
	}

	@Override
	public Optional<String> getLastModifiedBy() {
		return Optional.ofNullable(this.lastModifiedBy);
	}

	@Override
	public Optional<ZonedDateTime> getLastModifiedDate() {
		return Optional.ofNullable(this.lastModifiedDate);
	}
	
	

}
