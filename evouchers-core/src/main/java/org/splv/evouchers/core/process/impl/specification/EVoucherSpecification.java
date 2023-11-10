package org.splv.evouchers.core.process.impl.specification;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucher_;
import org.splv.evouchers.core.process.beans.in.EVoucherFilterBean;
import org.springframework.data.jpa.domain.Specification;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class EVoucherSpecification implements Specification<EVoucher> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	private final EVoucherFilterBean filterDTO;
	
	@Override
	public Predicate toPredicate(Root<EVoucher> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (filterDTO!= null) {

			final Expression<ZonedDateTime> donationDateExpression = root.get(EVoucher_.donationDate);

			List<Predicate> predicates = new ArrayList<>(4);
			if (filterDTO.getDateFrom() != null) {
				predicates.add(builder.greaterThanOrEqualTo(donationDateExpression, filterDTO.getDateFrom().with(LocalTime.MIN)));
			}
			if (filterDTO.getDateTo() != null) {
				predicates.add(builder.lessThanOrEqualTo(donationDateExpression, filterDTO.getDateTo().with(LocalTime.MAX)));
			}

			
			return predicates.isEmpty() ? builder.conjunction() : builder.and(predicates.toArray(new Predicate[0]));
		}

		return builder.conjunction();
	}


}
