package org.splv.evouchers.core.domain.support;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.splv.evouchers.core.Constants;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("java:S2160") // equals/hashCode handled in AbstractEntity
public abstract class AbstractBaseEntity extends AbstractAuditableVersionedEntity {

	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	protected Long id;
	

}
