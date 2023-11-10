package org.splv.evouchers.core.business;

import java.util.Optional;

import org.splv.evouchers.core.domain.DocumentContent;
import org.splv.evouchers.core.domain.DocumentMetadata;
import org.splv.evouchers.core.domain.DocumentType;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucherEvent;
import org.splv.evouchers.core.process.impl.specification.EVoucherSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EVoucherService {

	/**
	 * Find an eVoucher by its identifier
	 * @param id Identifier of the eVoucher
	 * @return an {@link Optional}} {@link EVoucher}
	 */
	Optional<EVoucher> findById(long id);
	
	/**
	 * Read an eVoucher by its identifier. Fails if not found.
	 * @param id Identifier of the eVoucher
	 * @return an {@link EVoucher}
	 */
	EVoucher readEVoucherById(long id);

	/**
	 * Save the eVoucher in parameters.
	 * @param eVoucher The eVoucher to save
	 * @return The saved eVoucher
	 */
	EVoucher createEVoucher(EVoucher eVoucher);
	
	/**
	 * Save the eVoucher in parameters.
	 * @param eVoucher The eVoucher to save
	 * @return The saved eVoucher
	 */
	EVoucher updateEVoucher(EVoucher eVoucher);
	
	/**
	 * archive the eVoucher in parameters.
	 * @param eVoucher The eVoucher to archive
	 * @return The saved eVoucher
	 */
	EVoucher archiveEVoucher(EVoucher eVoucher);
	/**
	 * restore the eVoucher with identifier in parameters.
	 * @param eVoucher The eVoucher to archive
	 * @return The saved eVoucher
	 */
	EVoucher restoreEVoucher(EVoucher eVoucher);

	/**
	 * Delete an eVoucher by its identifier.
	 * @param id Identifier of the eVoucher
	 */
	void deleteEVoucherById(long id);

	/**
	 * Find eVouchers complying with given specification.
	 * @param spec The specification
	 * @param pageable pagination information
	 * @return {@link Page} of {@link EVoucher}s
	 */
	Page<EVoucher> findEVouchers(EVoucherSpecification spec, Pageable pageable);
	
	/**
	 * Read an eVoucher document content by its identifier. Fails if not found.
	 * @param id Identifier of content
	 * @return {@link DocumentContent}
	 */
	DocumentContent readEVoucherDocumentContent(long id);
	
	/**
	 * Update the document content for the given eVoucher
	 * @param eVoucher eVoucher
	 * @param content document binary content
	 * @param metadata document metadata
	 * @return The printing event (printing means save a new printed document in database)
	 */
	EVoucherEvent updateEVoucherDocumentContent(EVoucher eVoucher, byte[] content, DocumentMetadata metadata);

	/**
	 * Save a printed document for the eVoucher.
	 * @param eVoucher The eVoucher
	 * @param content The binary document to save
	 * @param documentType The type of document
	 * @return a {@link DocumentContent}
	 */
	DocumentContent saveEVoucherPrintedDocument(EVoucher eVoucher, byte[] content, DocumentType documentType);
	
	/**
	 * Save a printed document for the eVoucher.
	 * @param eVoucher The eVoucher
	 * @param content The binary document to save
	 * @param documentType The type of document
	 * @return the printing event
	 */
	EVoucherEvent saveEVoucherPrintedDocumentWithEvent(EVoucher eVoucher, byte[] content, DocumentType documentType);

	/**
	 * Mark the eVoucher as sent
	 * @param eVoucher eVoucher to update
	 * @return the dispatch event
	 */
	EVoucherEvent markEVoucherAsSent(EVoucher eVoucher);


}
