package org.splv.evouchers.core.domain;

import static org.splv.evouchers.core.Constants.ADDRESS_SEARCH_FIELD;
import static org.splv.evouchers.core.Constants.DISTRIBUTION_YEAR_SEARCH_FIELD;
import static org.splv.evouchers.core.Constants.LOCATION_SEARCH_FIELD;
import static org.splv.evouchers.core.Constants.NAME_SEARCH_FIELD;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapKey;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.builtin.annotation.GeoPointBinding;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.RoutingBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.support.AbstractBaseEntity;
import org.splv.evouchers.core.tech.EVoucherSigningService;
import org.splv.evouchers.core.tech.impl.search.EVoucherStatusRoutingBinder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Indexed(routingBinder = @RoutingBinderRef(type = EVoucherStatusRoutingBinder.class)) 
@NamedEntityGraph(name = EVoucher.EVOUCHER_FULL_ENTITY_GRAPH, attributeNodes = { 
		@NamedAttributeNode(EVoucher_.EVENTS),
		@NamedAttributeNode(EVoucher_.DOCUMENTS) 
})
@SuppressWarnings("java:S2160") // no need to override equals method here
public class EVoucher extends AbstractBaseEntity {
	
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	public static final String EVOUCHER_FULL_ENTITY_GRAPH = "evoucher.full";

	private EVoucherDonorType donorType;
	private String donorName;
	private String donorLastname;
	private String donorFirstname;
	
	private String donorAddress;
	private String donorZipcode;
	private String donorCity;
	private String donorCountrycode = Constants.DEFAULT_LOCALE.getCountry();
	
	private String donorEmail;
	
	@GenericField(name = DISTRIBUTION_YEAR_SEARCH_FIELD, projectable = Projectable.YES, sortable = Sortable.YES) 
	private Integer distributionYear;
	
	@Embedded
	@GeoPointBinding(fieldName = LOCATION_SEARCH_FIELD, sortable = Sortable.YES)
	@AttributeOverride( name = "latitude", column = @Column(name = "donor_latitude"))
	@AttributeOverride( name = "longitude", column = @Column(name = "donor_longitude"))
	private Coordinates donorCoordinates;
	
	private BigDecimal amount;
	private ZonedDateTime donationDate;
	private EVoucherPaymentMethod paymentMethod;

	@Column(nullable = false, updatable = false)
	private String hashKey;
	
	private EVoucherIndexingStrategy indexingStrategy = EVoucherIndexingStrategy.INDEX;
	
	private EVoucherStatus status = EVoucherStatus.IN_PROGRESS;
	
	@Column(nullable = false, updatable = false)
	private Integer metadataVersion = 0;
	
	@OneToMany(mappedBy = DocumentMetadata_.E_VOUCHER, orphanRemoval = true, fetch = FetchType.LAZY)
	@MapKey(name = DocumentMetadata_.DOCUMENT_TYPE)
	private Map<DocumentType, DocumentMetadata> documents = new EnumMap<>(DocumentType.class);

	@OneToMany(mappedBy = DocumentMetadata_.E_VOUCHER, fetch = FetchType.LAZY, orphanRemoval = true)
	private final List<EVoucherEvent> events = new ArrayList<>();

	@PrePersist
    private void initializeHashkey() {
		if (isNew() || hashKey == null || hashKey.isBlank()) {
			this.hashKey = EVoucherSigningService.randomAlphanumeric(12);
		}
    }

	@FullTextField(name = NAME_SEARCH_FIELD)
	@IndexingDependency(derivedFrom = {
			@ObjectPath(@PropertyValue(propertyName = EVoucher_.DONOR_TYPE)),
			@ObjectPath(@PropertyValue(propertyName = EVoucher_.DONOR_NAME)),
			@ObjectPath(@PropertyValue(propertyName = EVoucher_.DONOR_LASTNAME)),
			@ObjectPath(@PropertyValue(propertyName = EVoucher_.DONOR_FIRSTNAME)),
	})
	public String getDisplayName() {
		return switch (donorType) {
		case INDIVIDUAL -> String.join(" ", this.getDonorFirstname(), this.getDonorLastname());
		case PROFESSIONAL -> this.getDonorName();
		case UNKNOWN -> throw new IllegalArgumentException("Cannot display name on unknown donor type");
		};
	}
	
	@FullTextField(name = ADDRESS_SEARCH_FIELD)
	@IndexingDependency(derivedFrom = {
			@ObjectPath(@PropertyValue(propertyName = EVoucher_.DONOR_ADDRESS)),
			@ObjectPath(@PropertyValue(propertyName = EVoucher_.DONOR_CITY)),
			@ObjectPath(@PropertyValue(propertyName = EVoucher_.DONOR_ZIPCODE)),
			@ObjectPath(@PropertyValue(propertyName = EVoucher_.DONOR_EMAIL)),
	})
	public String getAddressForSearch() {
		return String.join(" ", this.getDonorAddress(), this.getDonorCity(), this.getDonorZipcode(), this.getDonorEmail());
	}
	
	public String getFormattedReference() {
		return EVoucher.getFormattedReference(this);
	}
	
	public Optional<EVoucherEvent> getLastEvent() {
		return events.isEmpty() ? Optional.empty() : Optional.of(events.get(events.size()-1));
	}
	
	public boolean isEVoucherPrinted() {
		return this.getDocuments().containsKey(DocumentType.VOUCHER);
	}
	
	public static String getFormattedReference(EVoucher eVoucher) {
		return String.format("%d%06x", eVoucher.getDistributionYear(), eVoucher.getId());
	}
	
	public static Optional<Long> getIdFromReference(String reference) {
		try {
			return Optional.of(Long.valueOf(reference.substring(4), 16));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
	
}
