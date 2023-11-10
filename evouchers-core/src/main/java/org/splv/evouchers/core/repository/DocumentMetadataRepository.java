package org.splv.evouchers.core.repository;

import java.util.Optional;

import org.splv.evouchers.core.domain.DocumentMetadata;
import org.splv.evouchers.core.domain.DocumentType;
import org.splv.evouchers.core.repository.support.EntityManagerAwareJpaSpecificationExecutor;
import org.splv.evouchers.core.repository.support.PaginationJpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentMetadataRepository
		extends JpaRepository<DocumentMetadata, Long>, PaginationJpaSpecificationExecutor<DocumentMetadata, Long>,
		EntityManagerAwareJpaSpecificationExecutor<DocumentMetadata> {

	@Query(value = " SELECT dm FROM DocumentMetadata dm INNER JOIN FETCH dm.eVoucher WHERE dm.eVoucher.id = :eVoucherId AND dm.documentType = :documentType")
	Optional<DocumentMetadata> findByEVoucherIdAndDocumentType(@Param("eVoucherId") Long eVoucherId, @Param("documentType")  DocumentType documentType);
}
