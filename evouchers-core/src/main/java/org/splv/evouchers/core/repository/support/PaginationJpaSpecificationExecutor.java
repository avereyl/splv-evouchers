package org.splv.evouchers.core.repository.support;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.metamodel.SingularAttribute;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

@NoRepositoryBean
@SuppressWarnings("java:S119") // to keep consistent with Spring Data JPA API
public interface PaginationJpaSpecificationExecutor<T, ID extends Serializable> extends JpaSpecificationExecutor<T> {

	Page<ID> findEntitiesId(Pageable pageable);
    Page<ID> findEntitesId(Specification<T> spec, Pageable pageable);
    
    <X> Page<X> findEntitiesAttribute(SingularAttribute<T, X> attribute, Pageable pageable);
    <X> Page<X> findEntitiesAttribute(SingularAttribute<T, X> attribute, @Nullable Specification<T> spec, Pageable pageable);
	
	Page<T> findEntitiesThroughId(Pageable pageable);
    Page<T> findEntitiesThroughId(Specification<T> spec, Pageable pageable);
    
    List<T> findAllById(Iterable<ID> ids, Sort sort);
    
}
