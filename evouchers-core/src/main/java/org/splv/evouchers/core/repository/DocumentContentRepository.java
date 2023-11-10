package org.splv.evouchers.core.repository;

import org.splv.evouchers.core.domain.DocumentContent;
import org.splv.evouchers.core.repository.support.EntityManagerAwareJpaSpecificationExecutor;
import org.splv.evouchers.core.repository.support.PaginationJpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentContentRepository
		extends JpaRepository<DocumentContent, Long>, PaginationJpaSpecificationExecutor<DocumentContent, Long>,
		EntityManagerAwareJpaSpecificationExecutor<DocumentContent> {

}
