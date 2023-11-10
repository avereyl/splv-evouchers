package org.splv.evouchers.core.domain;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.support.AbstractEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@SuppressWarnings("java:S2160") // no need to override equals method here
public class EVoucherEvent extends AbstractEntity {

	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	protected Long id;
	
	@CreatedBy
	@Column(nullable=false, updatable = false)
	protected String createdBy;
	
	@CreatedDate
	@Column(nullable=false, updatable = false)
	protected ZonedDateTime createdDate;
	
	@Column(name="event_type", updatable = false, nullable = false)
	private EVoucherEventType type;
	
	@ManyToOne(optional = false)
	@JoinColumn(name="evoucher_id", nullable=false)
	private EVoucher eVoucher;

	public static EVoucherEvent print(EVoucher eVoucher) {
		return new EVoucherEvent(EVoucherEventType.PRINTING, eVoucher);
	}
	
	public static EVoucherEvent dispatch(EVoucher eVoucher) {
		return new EVoucherEvent(EVoucherEventType.DISPATCH, eVoucher);
	}
	
	public static EVoucherEvent create(EVoucher eVoucher) {
		return new EVoucherEvent(EVoucherEventType.CREATION, eVoucher);
	}
	
	public static EVoucherEvent update(EVoucher eVoucher) {
		return new EVoucherEvent(EVoucherEventType.UPDATE, eVoucher);
	}
	
	public static EVoucherEvent archive(EVoucher eVoucher) {
		return new EVoucherEvent(EVoucherEventType.ARCHIVING, eVoucher);
	}
	public static EVoucherEvent restore(EVoucher eVoucher) {
		return new EVoucherEvent(EVoucherEventType.RESTORING, eVoucher);
	}

	public EVoucherEvent(EVoucherEventType type, EVoucher eVoucher) {
		super();
		this.type = type;
		this.eVoucher = eVoucher;
	}
	
	public Optional<String> getCreatedBy() {
		return Optional.ofNullable(this.createdBy);
	}

	public Optional<ZonedDateTime> getCreatedDate() {
		return Optional.ofNullable(this.createdDate);
	}

	
}
