
package org.splv.evouchers.core.process.impl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.business.EVoucherService;
import org.splv.evouchers.core.business.beans.Attachment;
import org.splv.evouchers.core.domain.DocumentContent;
import org.splv.evouchers.core.domain.DocumentMetadata;
import org.splv.evouchers.core.domain.DocumentType;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucherEvent;
import org.splv.evouchers.core.domain.EVoucherEventType;
import org.splv.evouchers.core.domain.EVoucherStatus;
import org.splv.evouchers.core.domain.support.AbstractAuditableEntity_;
import org.splv.evouchers.core.process.EVoucherProcessService;
import org.splv.evouchers.core.process.beans.converter.EVoucherConverter;
import org.splv.evouchers.core.process.beans.converter.EVoucherObjectConverter;
import org.splv.evouchers.core.process.beans.converter.EVoucherSaveBeanConverter;
import org.splv.evouchers.core.process.beans.in.EVoucherFilterBean;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;
import org.splv.evouchers.core.process.beans.in.EVoucherSearchBean;
import org.splv.evouchers.core.process.beans.out.EVoucherEventObject;
import org.splv.evouchers.core.process.beans.out.EVoucherObject;
import org.splv.evouchers.core.process.beans.out.EVoucherPrintObject;
import org.splv.evouchers.core.process.beans.out.EVoucherValidationResultObject;
import org.splv.evouchers.core.process.exception.EVoucherPrintingException;
import org.splv.evouchers.core.process.impl.specification.EVoucherSortPropertyMapper;
import org.splv.evouchers.core.process.impl.specification.EVoucherSpecification;
import org.splv.evouchers.core.tech.EVoucherMailingService;
import org.splv.evouchers.core.tech.EVoucherPrintingService;
import org.splv.evouchers.core.tech.EVoucherSearchingService;
import org.splv.evouchers.core.tech.EVoucherSigningService;
import org.splv.evouchers.core.tech.exception.SigningException;
import org.splv.evouchers.core.tech.exception.SigningException.Reason;
import org.splv.evouchers.core.tech.impl.support.PrintingHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@Transactional
@RequiredArgsConstructor
public class EVoucherProcessServiceImpl implements EVoucherProcessService {
	
	private final EVoucherService eVoucherService;
	private final EVoucherSearchingService searchingService;
	private final EVoucherMailingService mailingService;
	private final EVoucherPrintingService printingService;
	private final EVoucherSigningService signingService;
	private final EVoucherConverter eVoucherConverter = new EVoucherConverter();
	private final EVoucherSaveBeanConverter eVoucherSaveBeanConverter = new EVoucherSaveBeanConverter();
	private final EVoucherObjectConverter eVoucherObjectConverter = new EVoucherObjectConverter();
	

	@Override
	public EVoucherObject createEVoucher(EVoucherSaveBean eVoucherSaveBean) {
		EVoucher eVoucher = eVoucherConverter.convert(eVoucherSaveBean);
		return eVoucherObjectConverter.convert(eVoucherService.createEVoucher(eVoucher));
	}
	
	@Override
	public EVoucherObject readEVoucher(long id) {
		return eVoucherObjectConverter.convert(eVoucherService.readEVoucherById(id));
	}

	@Override
	public EVoucherObject updateEVoucher(long id, EVoucherSaveBean eVoucherSaveBean) {
		EVoucher eVoucher = eVoucherService.readEVoucherById(id);
		eVoucher = eVoucherConverter.merge(eVoucherSaveBean, eVoucher);
		eVoucher = eVoucherService.updateEVoucher(eVoucher);
		
		if (eVoucher.isEVoucherPrinted()) {
			DocumentMetadata metadata = eVoucher.getDocuments().get(DocumentType.VOUCHER);
			try (ByteArrayOutputStream baos = printingService.printEVoucher(eVoucher, Constants.DEFAULT_LOCALE);) {
				// save created doc
				EVoucherEvent printEvent = eVoucherService.updateEVoucherDocumentContent(eVoucher, baos.toByteArray(), metadata);
				eVoucher.getEvents().add(printEvent);
			} catch (IOException e) {
				throw new EVoucherPrintingException(id, "Unable to print the eVoucher.", e);
			}
		}
		return eVoucherObjectConverter.convert(eVoucher);
	}

	@Override
	public void deleteEVoucher(long id) {
		eVoucherService.deleteEVoucherById(id);
	}

	@Override
	public EVoucherObject archiveEVoucher(long id) {
		EVoucher eVoucher = eVoucherService.readEVoucherById(id);
		return eVoucherObjectConverter.convert(eVoucherService.archiveEVoucher(eVoucher));
	}
	
	@Override
	public EVoucherObject restoreEVoucher(long id) {
		EVoucher eVoucher = eVoucherService.readEVoucherById(id);
		return eVoucherObjectConverter.convert(eVoucherService.restoreEVoucher(eVoucher));
	}
	
	@Override
	public EVoucherPrintObject previewEVoucher(long id, boolean isForcedPrinting) {
		final EVoucher workingEVoucher = eVoucherService.readEVoucherById(id);
		// 
		byte[] content = null;
		if (workingEVoucher.isEVoucherPrinted() && !isForcedPrinting) {
			// load from db
			DocumentMetadata dm = workingEVoucher.getDocuments().get(DocumentType.VOUCHER);
			DocumentContent documentContent = eVoucherService.readEVoucherDocumentContent(dm.getId());
			content = documentContent.getContent();
		} else {
			// build document
			try (ByteArrayOutputStream baos = printingService.printEVoucher(workingEVoucher, Constants.DEFAULT_LOCALE);) {
				// save created doc
				content = baos.toByteArray();
				eVoucherService.saveEVoucherPrintedDocument(workingEVoucher, content, DocumentType.VOUCHER);
			} catch (IOException e) {
				throw new EVoucherPrintingException(id, "Unable to print the eVoucher.", e);
			}
		}
		// return printed document
		return buildPrintedEvoucherObject(workingEVoucher, content, DocumentType.VOUCHER);
	}

	@Override
	public Flux<EVoucherEventObject> dispatchEVoucher(long id, Set<String> to, Set<String> cc) {
		// load, print and send
		Optional<EVoucher> optEVoucher = eVoucherService.findById(id);
		if (optEVoucher.isEmpty() || !Set.of(EVoucherStatus.IN_PROGRESS, EVoucherStatus.SENT, EVoucherStatus.UPDATED)
				.contains(optEVoucher.get().getStatus())) {
			return Flux.just(EVoucherEventObject.buildEvent(EVoucherEventType.ERROR));
		}
		
		final EVoucher eVoucher = optEVoucher.get();
		return Flux.create(sink -> {
			// ACK
			sink.next(EVoucherEventObject.buildEvent(EVoucherEventType.ACK));
			
			// PRINTING
			try {
				byte[] content = null;
				if (eVoucher.isEVoucherPrinted()) {
					// load from db
					DocumentMetadata dm = eVoucher.getDocuments().get(DocumentType.VOUCHER);
					DocumentContent documentContent = eVoucherService.readEVoucherDocumentContent(dm.getId());
					content = documentContent.getContent();
				} else {
					//print
					try (ByteArrayOutputStream baos = printingService.printEVoucher(eVoucher, Constants.DEFAULT_LOCALE);) {
						content = baos.toByteArray();	
						// and save printed document
						EVoucherEvent printingEvent = eVoucherService.saveEVoucherPrintedDocumentWithEvent(eVoucher, content, DocumentType.VOUCHER);
						sink.next(eVoucherObjectConverter.convert(printingEvent));
					}
				}
				//mark eVoucher as sent
				EVoucherEvent dispatchEvent = eVoucherService.markEVoucherAsSent(eVoucher);
				
				// DISPATCH
				Attachment attachment = new Attachment(content, PrintingHelper.computeVoucherFilename(eVoucher), Constants.DEFAULT_EVOUCHER_PRINT_MIME_TYPE);
				mailingService.sendEVoucherPrint(eVoucher, Constants.DEFAULT_LOCALE, List.of(attachment), to, cc);
				sink.next(eVoucherObjectConverter.convert(dispatchEvent));
				sink.complete();

			} catch (Exception e) {
				sink.next(EVoucherEventObject.buildEvent(EVoucherEventType.ERROR));
				sink.complete();
			}
		});
	}

	@Override
	public EVoucherObject processEVoucher(EVoucherSaveBean eVoucherSaveBean, Set<String> to, Set<String> cc) {
		// create, print and send
		// CREATION
		final EVoucher eVoucher = eVoucherService.createEVoucher(eVoucherConverter.convert(eVoucherSaveBean));
		// PRINTING
		try (ByteArrayOutputStream baos = printingService.printEVoucher(eVoucher, Constants.DEFAULT_LOCALE);) {
			//save document and event
			eVoucherService.saveEVoucherPrintedDocumentWithEvent(eVoucher, baos.toByteArray(), DocumentType.VOUCHER);

			//mark eVoucher as sent
			eVoucherService.markEVoucherAsSent(eVoucher);
			
			// DISPATCH
			Attachment attachment = new Attachment(baos.toByteArray(), PrintingHelper.computeVoucherFilename(eVoucher), Constants.DEFAULT_EVOUCHER_PRINT_MIME_TYPE);
			mailingService.sendEVoucherPrint(eVoucher, Constants.DEFAULT_LOCALE, List.of(attachment), to, cc);

		} catch (IOException e) {
			throw new EVoucherPrintingException(eVoucher.getId(), "Unable to process the eVoucher.", e);
		}

		return eVoucherObjectConverter.convert(eVoucher);
	}

	@Override
	public Flux<EVoucherEventObject> processEVoucherWithEvents(EVoucherSaveBean eVoucherSaveBean, Set<String> to, Set<String> cc) {
		// create, print and send
		return Flux.create(sink -> {
			// ACK
			sink.next(EVoucherEventObject.buildEvent(EVoucherEventType.ACK));
			
			// CREATION
			final EVoucher eVoucher = eVoucherService.createEVoucher(eVoucherConverter.convert(eVoucherSaveBean));
			Optional<EVoucherEvent> optEvent = eVoucher.getEvents().stream()
					.filter(ev -> EVoucherEventType.CREATION.equals(ev.getType())).findFirst();
			optEvent.ifPresent(ev -> 
				sink.next(eVoucherObjectConverter.convert(ev))
			);
			
			// PRINTING
			try (ByteArrayOutputStream baos = printingService.printEVoucher(eVoucher, Constants.DEFAULT_LOCALE);) {
				
				// and save printed document
				EVoucherEvent printingEvent = eVoucherService.saveEVoucherPrintedDocumentWithEvent(eVoucher, baos.toByteArray(), DocumentType.VOUCHER);
				sink.next(eVoucherObjectConverter.convert(printingEvent));
				
				//mark eVoucher as sent
				EVoucherEvent dispatchEvent = eVoucherService.markEVoucherAsSent(eVoucher);
				
				// DISPATCH
				Attachment attachment = new Attachment(baos.toByteArray(), PrintingHelper.computeVoucherFilename(eVoucher), Constants.DEFAULT_EVOUCHER_PRINT_MIME_TYPE);
				mailingService.sendEVoucherPrint(eVoucher, Constants.DEFAULT_LOCALE, List.of(attachment), to, cc);
				sink.next(eVoucherObjectConverter.convert(dispatchEvent));
				sink.complete();
				
			} catch (Exception e) {
				sink.next(EVoucherEventObject.buildEvent(EVoucherEventType.ERROR));
				sink.complete();
			}
			
		});
	}
	
	@Override
	public Optional<EVoucherObject> findEVoucherById(long id) {
		return eVoucherService.findById(id).map(eVoucherObjectConverter::convert);
	}
	
	@Override
	public Page<EVoucherObject> findEVouchers(EVoucherFilterBean eVoucherFilterBean, Pageable pageable) {
		EVoucherSpecification spec = new EVoucherSpecification(eVoucherFilterBean);
    	// translate order properties related to DTO into path related to domain object 
    	List<Order> orders = pageable.getSort().stream().filter(EVoucherSortPropertyMapper::isSortOrderSupported).map(EVoucherSortPropertyMapper::map).toList();
    	// set a default order if none set
    	PageRequest pr = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), orders.isEmpty() ? Sort.by(Direction.DESC, AbstractAuditableEntity_.CREATED_DATE) : Sort.by(orders));
        // find and convert eVouchers
    	Page<EVoucher> page = eVoucherService.findEVouchers(spec, pr);
        List<EVoucherObject> dtos = page.stream().map(eVoucherObjectConverter::convert).toList();
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
	}
	
	@Override
	public List<EVoucherSaveBean> findEVouchersTemplates(EVoucherSearchBean eVoucherSearchBean) {
		List<EVoucher> results = null;
		if (eVoucherSearchBean.getLatitude() != null && eVoucherSearchBean.getLongitude() != null) {
			results = searchingService.search(eVoucherSearchBean.getLatitude(), eVoucherSearchBean.getLongitude(),
					Constants.DEFAULT_SEARCH_DISTANCE, eVoucherSearchBean.getNbHits());
		} else {
			results = searchingService.search(eVoucherSearchBean.getQuery(), eVoucherSearchBean.getNbHits());
		}
		return results.stream().map(eVoucherSaveBeanConverter::convert).toList();
	}

	@Override
	public EVoucherValidationResultObject validateEVoucherPrint(String fingerprint) {
		try {
			// compute and compare signature
			return signingService.verifyEVoucherSignature(fingerprint) ? EVoucherValidationResultObject.valid()
					: EVoucherValidationResultObject.invalid();
		} catch (SigningException e) {
			return e.getReason() == Reason.UNSUPPORTED ? EVoucherValidationResultObject.unsupported()
					: EVoucherValidationResultObject.error();
		}
	}
	
	/**
	 * Build a {@link EVoucherPrintObject} from the eVoucher and the binary content in parameter.
	 * @param eVoucher The related eVoucher
	 * @param bytes The printed content
	 * @param documentType Type of document
	 * @return
	 */
	private EVoucherPrintObject buildPrintedEvoucherObject(EVoucher eVoucher, byte[] bytes, DocumentType documentType) {
		EVoucherPrintObject printObject = new EVoucherPrintObject();
		printObject.setId(eVoucher.getId());
		printObject.setVersion(eVoucher.getVersion());
		printObject.setData(bytes);
		printObject.setFilename(PrintingHelper.computeVoucherFilename(eVoucher));
		printObject.setDocumentType(documentType);
		return printObject;
	}

	
}
