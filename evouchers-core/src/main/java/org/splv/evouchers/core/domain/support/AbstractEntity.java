package org.splv.evouchers.core.domain.support;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import org.splv.evouchers.core.Constants;
import org.springframework.data.domain.Persistable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Persistable<Long>, Serializable {

	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	@Transient
	protected final UUID uuid = UUID.randomUUID();

	@Override
	public boolean isNew() {
		return getId() == null;
	}

	@Override
	public int hashCode() {
		return getId() == null ? Objects.hash(getUuid()) : Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final var other = this.getClass().cast(obj);
		return (getId() != null && Objects.equals(getId(), other.getId())) || Objects.equals(getUuid(), other.getUuid());
	}
	
	
	

}
