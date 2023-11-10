package org.splv.evouchers.core.repository.support;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Parameter;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.SingularAttribute;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Extension of the {@link SimpleJpaRepository} to prevent
 * 
 * <pre>
 * HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
 * </pre>
 * 
 * which affects application's performance.
 * 
 * @author AVEREYL
 *
 * @param <T>  the type of the entity to handle
 * @param <ID> the type of the entity's identifier
 */
@Slf4j
@SuppressWarnings("java:S119") // to keep consistent with Spring Data JPA API
public class ExtendedSimpleJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements PaginationJpaSpecificationExecutor<T, ID>, EntityManagerAwareJpaSpecificationExecutor<T> {

	private final EntityManager entityManager;
	private final JpaEntityInformation<T, ID> entityInformation;

	public ExtendedSimpleJpaRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
	}

	@Override
	public <X> Page<X> findEntitiesAttribute(SingularAttribute<T, X> attribute, Pageable pageable) {
		return findEntitiesAttribute(attribute, null, pageable);
	}

	@Override
	public <X> Page<X> findEntitiesAttribute(SingularAttribute<T, X> attribute, @Nullable Specification<T> spec,
			Pageable pageable) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<X> criteriaQuery = criteriaBuilder.createQuery(attribute.getJavaType());
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		Subquery<X> subquery = criteriaQuery.subquery(attribute.getJavaType());
		Root<T> subRoot = subquery.from(this.getDomainClass());
		subquery.select(subRoot.get(attribute));

		// Handling specification
		if (spec != null) {
			Predicate predicate = spec.toPredicate(subRoot, criteriaQuery, criteriaBuilder);
			if (predicate != null) {
				subquery.where(predicate);
			}
		}

		Root<T> root = criteriaQuery.from(this.getDomainClass());
		// Get the selected attribute only
		Path<X> selection = (root.get(attribute));
		criteriaQuery.select(selection).where(selection.in(subquery));

		// Update Sorting
		Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
		if (sort.isSorted()) {
			criteriaQuery.orderBy(toOrders(sort, root, criteriaBuilder));
		}

		TypedQuery<X> typedQuery = this.entityManager.createQuery(criteriaQuery);

		// Update Pagination attributes
		if (pageable.isPaged()) {
			typedQuery.setFirstResult((int) pageable.getOffset());
			typedQuery.setMaxResults(pageable.getPageSize());
		}

		// handle count query
		Root<T> countRoot = countQuery.from(this.getDomainClass());
		if (spec != null) {
			Predicate predicate = spec.toPredicate(countRoot, countQuery, criteriaBuilder);
			if (predicate != null) {
				countQuery.where(predicate);
			}
		}
		countQuery.select(criteriaBuilder.count(countRoot.get(attribute)));
		// Remove all Orders the Specifications might have applied
		countQuery.orderBy(Collections.<Order>emptyList());

		return PageableExecutionUtils.getPage(typedQuery.getResultList(), pageable,
				() -> executeCountQuery(entityManager.createQuery(countQuery)));
	}

	@Override
	public Page<ID> findEntitiesId(Pageable pageable) {
		return findEntitesId(null, pageable);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Page<ID> findEntitesId(@Nullable Specification<T> spec, Pageable pageable) {
		return findEntitiesAttribute((SingularAttribute<T, ID>) this.entityInformation.getIdAttribute(), spec, pageable);
	}

	protected static long executeCountQuery(TypedQuery<Long> query) {
		Assert.notNull(query, "TypedQuery must not be null!");
		return query.getResultList().stream().reduce(0L, (a, b) -> a + b).longValue();
	}

	// Method to prevent HHH000104: firstResult/maxResults specified with collection
	// fetch; applying in memory! which affects application's performance
	@Override
	public Page<T> findEntitiesThroughId(Pageable pageable) {
		return findEntitiesThroughId(null, pageable);
	}

	// Method to prevent HHH000104: firstResult/maxResults specified with collection
	// fetch; applying in memory! which affects application's performance
	@Override
	public Page<T> findEntitiesThroughId(@Nullable Specification<T> spec, Pageable pageable) {
		Page<ID> idsPage = findEntitiesId(pageable);
		List<ID> ids = idsPage.getContent();
		List<T> content = CollectionUtils.isEmpty(ids) ? List.of()
				: findAllById(ids, pageable.getSortOr(Sort.unsorted()));
		return PageableExecutionUtils.getPage(content, pageable, idsPage::getTotalElements);
	}

	@Override
	public List<T> findAllById(Iterable<ID> ids, Sort sort) {

		Assert.notNull(ids, "Ids must not be null!");

		if (!ids.iterator().hasNext()) {
			return Collections.emptyList();
		}

		if (entityInformation.hasCompositeId()) {

			List<T> results = new ArrayList<>();

			for (ID id : ids) {
				findById(id).ifPresent(results::add);
			}

			return results;
		}

		Collection<ID> idCollection = Streamable.of(ids).toList();

		ByIdsSpecification<T> specification = new ByIdsSpecification<>(entityInformation);
		TypedQuery<T> query = getQuery(specification, sort);

		return query.setParameter(specification.parameter, idCollection).getResultList();

	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public <S extends T> S detach(S entity) {
		entityManager.detach(entity);
		return entity;
	}

	@Override
	public <S extends T> S refresh(S entity) {
		entityManager.refresh(entity);
		return entity;
	}

	@Override
	public void clear2ndLevelCache() {
		Cache cache = entityManager.getEntityManagerFactory().getCache();
		if (cache != null) {
			cache.evictAll();
			log.info("Second level cache cleared");
		} else {
			log.warn("No cache to clear !");
		}
	}

	/**
	 * Copy from org.springframework.data.jpa.repository.support.SimpleJpaRepository
	 * 
	 * Specification that gives access to the {@link Parameter} instance used to
	 * bind the ids for {@link SimpleJpaRepository#findAllById(Iterable)}.
	 * Workaround for OpenJPA not binding collections to in-clauses correctly when
	 * using by-name binding.
	 *
	 * @see <a href=
	 *      "https://issues.apache.org/jira/browse/OPENJPA-2018?focusedCommentId=13924055">OPENJPA-2018</a>
	 * @author Oliver Gierke
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "java:S1948", "java:S1905" })
	private static final class ByIdsSpecification<T> implements Specification<T> {

		private static final long serialVersionUID = 1L;

		private final JpaEntityInformation<T, ?> entityInformation;

		@Nullable
		ParameterExpression<Collection<?>> parameter;

		ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
			this.entityInformation = entityInformation;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.
		 * persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery,
		 * javax.persistence.criteria.CriteriaBuilder)
		 */
		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

			Path<?> path = root.get(entityInformation.getIdAttribute());
			parameter = (ParameterExpression<Collection<?>>) (ParameterExpression) cb.parameter(Collection.class);
			return path.in(parameter);
		}
	}
}
