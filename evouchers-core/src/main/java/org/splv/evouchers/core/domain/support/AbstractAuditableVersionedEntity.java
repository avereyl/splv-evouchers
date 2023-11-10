package org.splv.evouchers.core.domain.support;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import org.splv.evouchers.core.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@SuppressWarnings("java:S2160") // equals/hashCode handled in AbstractEntity
public abstract class AbstractAuditableVersionedEntity extends AbstractAuditableEntity {

	private static final long serialVersionUID = Constants.SERIAL_VERSION;

	@Version
	@Column(nullable = false)
	protected Long version;

}
