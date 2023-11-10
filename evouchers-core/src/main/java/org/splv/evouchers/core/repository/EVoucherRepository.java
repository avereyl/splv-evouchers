package org.splv.evouchers.core.repository;

import java.util.List;
import java.util.Optional;

import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.repository.support.EntityManagerAwareJpaSpecificationExecutor;
import org.splv.evouchers.core.repository.support.PaginationJpaSpecificationExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EVoucherRepository extends JpaRepository<EVoucher, Long>,
		PaginationJpaSpecificationExecutor<EVoucher, Long>, EntityManagerAwareJpaSpecificationExecutor<EVoucher> {

	@Override
	@EntityGraph(EVoucher.EVOUCHER_FULL_ENTITY_GRAPH)
	Optional<EVoucher> findById(Long id);

	@Override
	@EntityGraph(EVoucher.EVOUCHER_FULL_ENTITY_GRAPH)
	List<EVoucher> findAllById(Iterable<Long> ids);

	@Override
	@EntityGraph(EVoucher.EVOUCHER_FULL_ENTITY_GRAPH)
	List<EVoucher> findAllById(Iterable<Long> ids, Sort sort);

	@Override
	@EntityGraph(EVoucher.EVOUCHER_FULL_ENTITY_GRAPH)
	List<EVoucher> findAll(Specification<EVoucher> spec);

}
