package org.splv.evouchers.core.business.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.splv.evouchers.core.business.EVoucherService;
import org.splv.evouchers.core.business.exception.EVoucherInvalidStatusException;
import org.splv.evouchers.core.business.exception.EVoucherNotFoundException;
import org.splv.evouchers.core.business.exception.EVoucherPrintNotFoundException;
import org.splv.evouchers.core.config.properties.VoucherGenerationProperties;
import org.splv.evouchers.core.domain.DocumentContent;
import org.splv.evouchers.core.domain.DocumentMetadata;
import org.splv.evouchers.core.domain.DocumentType;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucherEvent;
import org.splv.evouchers.core.domain.EVoucherStatus;
import org.splv.evouchers.core.process.impl.specification.EVoucherSpecification;
import org.splv.evouchers.core.repository.DocumentContentRepository;
import org.splv.evouchers.core.repository.DocumentMetadataRepository;
import org.splv.evouchers.core.repository.EVoucherEventRepository;
import org.splv.evouchers.core.repository.EVoucherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EVoucherServiceImpl implements EVoucherService {

	private final EVoucherRepository eVoucherRepository;
	private final EVoucherEventRepository eVoucherEventRepository;
	private final DocumentContentRepository documentContentRepository;
	private final DocumentMetadataRepository documentMetadataRepository;
	private final VoucherGenerationProperties voucherProperties;

	@Override
	public Optional<EVoucher> findById(long id) {
		return eVoucherRepository.findById(id);
	}
	
	@Override
	public EVoucher readEVoucherById(long id) {
		return findById(id).orElseThrow(() -> new EVoucherNotFoundException(id, "Unable to read the eVoucher."));
	}

	@Override
	public EVoucher createEVoucher(EVoucher eVoucher) {
		eVoucher.setVersion(null);
		eVoucher.setMetadataVersion(voucherProperties.getMetadataCurrentVersion());
		eVoucher = eVoucherRepository.saveAndFlush(eVoucher);
		EVoucherEvent creationEvent = eVoucherEventRepository.save(EVoucherEvent.create(eVoucher));
		eVoucher.getEvents().add(creationEvent);
		return eVoucher;
	}
	@Override
	public EVoucher updateEVoucher(EVoucher eVoucher) {
		switch (eVoucher.getStatus()) {
		case IN_PROGRESS -> eVoucher = eVoucherRepository.save(eVoucher);
		case SENT -> {
			eVoucher.setStatus(EVoucherStatus.UPDATED);
			eVoucher = eVoucherRepository.save(eVoucher);
		}
		case UPDATED -> eVoucher = eVoucherRepository.save(eVoucher);
		default -> throw new EVoucherInvalidStatusException(eVoucher.getId(), eVoucher.getStatus(),
				Set.of(EVoucherStatus.IN_PROGRESS, EVoucherStatus.SENT, EVoucherStatus.UPDATED));
		}
		EVoucherEvent event = eVoucherEventRepository.save(EVoucherEvent.update(eVoucher));
		eVoucher.getEvents().add(event);
		return eVoucher;
	}


	@Override
	public void deleteEVoucherById(long id) {
		var expectedStatuses = Set.of(EVoucherStatus.IN_PROGRESS, EVoucherStatus.ARCHIVED);
		eVoucherRepository.findById(id).ifPresent(ev -> {
			if (!expectedStatuses.contains(ev.getStatus())) {
				throw new EVoucherInvalidStatusException(id, ev.getStatus(), expectedStatuses);
			}
			ev.getDocuments().forEach((t, m) -> documentContentRepository.deleteById(m.getId()));
			eVoucherRepository.deleteById(id);
		});
	}

	@Override
	public Page<EVoucher> findEVouchers(EVoucherSpecification spec, Pageable pageable) {
		Page<Long> idsPage = eVoucherRepository.findEntitesId(spec, pageable);
		List<Long> ids = idsPage.getContent();
		List<EVoucher> content = CollectionUtils.isEmpty(ids) ? List.of()
				: eVoucherRepository.findAllById(ids, pageable.getSortOr(Sort.unsorted()));
		return PageableExecutionUtils.getPage(content, pageable, idsPage::getTotalElements);
	}

	@Override
	public DocumentContent readEVoucherDocumentContent(long id) {
		return documentContentRepository.findById(id).orElseThrow(() -> new EVoucherPrintNotFoundException(id, "Unable to read the eVoucher document."));
	}

	@Override
	public DocumentContent saveEVoucherPrintedDocument(EVoucher eVoucher, byte[] content, DocumentType documentType) {
		
		var documentContent = new DocumentContent(content);
		DocumentMetadata metadata = eVoucher.getDocuments().getOrDefault(DocumentType.VOUCHER, new DocumentMetadata());
		if (metadata.getId() != null) { //overriding
			documentContent.setId(metadata.getId());
		}
		metadata.setDocumentType(documentType);
		metadata.setEVoucher(eVoucher);
		metadata = documentMetadataRepository.saveAndFlush(metadata);
		documentContent.setMetadata(metadata);
		
		eVoucherEventRepository.save(EVoucherEvent.print(eVoucher));
		return documentContentRepository.save(documentContent);
	}

	@Override
	public EVoucherEvent saveEVoucherPrintedDocumentWithEvent(EVoucher eVoucher, byte[] content,
			DocumentType documentType) {
		var documentContent = new DocumentContent(content);
		DocumentMetadata metadata = new DocumentMetadata();
		metadata.setDocumentType(documentType);
		metadata.setEVoucher(eVoucher);
		metadata = documentMetadataRepository.saveAndFlush(metadata);
		documentContent.setMetadata(metadata);
		this.documentContentRepository.save(documentContent);
		return eVoucherEventRepository.save(EVoucherEvent.print(eVoucher));
	}

	@Override
	public EVoucherEvent updateEVoucherDocumentContent(EVoucher eVoucher, byte[] content, DocumentMetadata metadata) {
		DocumentContent dc = new DocumentContent();
		dc.setId(metadata.getId());
		dc.setMetadata(metadata);
		dc.setContent(content);
		documentContentRepository.save(dc);
		return eVoucherEventRepository.save(EVoucherEvent.print(eVoucher));
	}

	@Override
	public EVoucherEvent markEVoucherAsSent(EVoucher eVoucher) {
		eVoucher.setStatus(EVoucherStatus.SENT);
		eVoucherRepository.save(eVoucher);
		return eVoucherEventRepository.save(EVoucherEvent.dispatch(eVoucher));
	}
	
	@Override
	public EVoucher archiveEVoucher(EVoucher eVoucher) {
		if (EVoucherStatus.SENT.equals(eVoucher.getStatus())) {
			eVoucher.setStatus(EVoucherStatus.ARCHIVED);
			eVoucher = eVoucherRepository.save(eVoucher);
			EVoucherEvent archivingEvent = eVoucherEventRepository.save(EVoucherEvent.archive(eVoucher));
			eVoucher.getEvents().add(archivingEvent);
		} else if (!EVoucherStatus.ARCHIVED.equals(eVoucher.getStatus())) {
			throw new EVoucherInvalidStatusException(eVoucher.getId(), eVoucher.getStatus(), Set.of(EVoucherStatus.SENT));
		}
		return eVoucher;
	}
	
	@Override
	public EVoucher restoreEVoucher(EVoucher eVoucher) {
		if (EVoucherStatus.ARCHIVED.equals(eVoucher.getStatus())) {
			eVoucher.setStatus(EVoucherStatus.SENT);
			eVoucher = eVoucherRepository.save(eVoucher);
			EVoucherEvent restoringEvent = eVoucherEventRepository.save(EVoucherEvent.restore(eVoucher));
			eVoucher.getEvents().add(restoringEvent);
		} else if (!EVoucherStatus.SENT.equals(eVoucher.getStatus())) {
			throw new EVoucherInvalidStatusException(eVoucher.getId(), eVoucher.getStatus(), Set.of(EVoucherStatus.SENT));
		}
		return eVoucher;
	}
	

}
