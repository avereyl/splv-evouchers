package org.splv.evouchers.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.support.AbstractEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DOCUMENT_CONTENT")
@NoArgsConstructor
@SuppressWarnings("java:S2160") // no need to override equals method here
public class DocumentContent extends AbstractEntity {

	private static final long serialVersionUID = Constants.SERIAL_VERSION;

	@Id
	@Column(name = "ID", updatable = false, nullable = false)
	protected Long id;

	@Lob
	@NotNull
	private byte[] content;

	@JsonBackReference
	@NotNull
	@Fetch(value = FetchMode.JOIN)
	@OneToOne(orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id", nullable = false, unique = true, updatable = false)
	private DocumentMetadata metadata;

	public DocumentContent(byte[] content) {
		this.content = content;
	}
}
