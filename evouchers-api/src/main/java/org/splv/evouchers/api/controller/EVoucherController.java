package org.splv.evouchers.api.controller;

import static org.splv.evouchers.api.Constants.DEFAULT_API_CONTENT_TYPE_V1;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import org.splv.evouchers.api.controller.exception.InvalidEVoucherStatusParameterException;
import org.splv.evouchers.core.business.exception.EVoucherNotFoundException;
import org.splv.evouchers.core.domain.EVoucherStatus;
import org.splv.evouchers.core.process.EVoucherProcessService;
import org.splv.evouchers.core.process.beans.in.EVoucherFilterBean;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;
import org.splv.evouchers.core.process.beans.in.EVoucherSearchBean;
import org.splv.evouchers.core.process.beans.in.EVoucherStatusBean;
import org.splv.evouchers.core.process.beans.in.EVoucherValidationBean;
import org.splv.evouchers.core.process.beans.out.EVoucherEventObject;
import org.splv.evouchers.core.process.beans.out.EVoucherObject;
import org.splv.evouchers.core.process.beans.out.EVoucherPrintObject;
import org.splv.evouchers.core.process.beans.out.EVoucherValidationResultObject;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class EVoucherController {

	private final EVoucherProcessService eVoucherProcessService;

	/**
	 * Find an eVoucher by its id
	 * 
	 * @param id Identifier of the eVoucher
	 * @return an {@link EVoucherObject}
	 */
	@PageableAsQueryParam
	@GetMapping(value = "/evouchers/", produces = { DEFAULT_API_CONTENT_TYPE_V1 })
	public ResponseEntity<Page<EVoucherObject>> findEVouchers(@ParameterObject Pageable pageable,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final ZonedDateTime dateFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final ZonedDateTime dateTo) {

		EVoucherFilterBean filterDTO = new EVoucherFilterBean();
		filterDTO.setDateFrom(dateFrom);
		filterDTO.setDateTo(dateTo);

		return ResponseEntity.ok(eVoucherProcessService.findEVouchers(filterDTO, pageable));
	}

	/**
	 * Find eVoucher save bean to be used as template for a new eVoucher creations.
	 * 
	 * @param query     String query for full text search
	 * @param latitude  Latitude for geolocation search
	 * @param longitude Longitude for geolocation search
	 * @param dateFrom  Date "from" for filtering on donation date
	 * @param dateTo    Date "to" for filtering on donation date
	 * @return an {@link EVoucherObject}
	 */
	@GetMapping(value = "/evouchers/templates/", produces = { DEFAULT_API_CONTENT_TYPE_V1 })
	public ResponseEntity<List<EVoucherSaveBean>> findEVouchersTemplates(
			@RequestParam(required = false) final String query,
			@RequestParam(required = false) final Double latitude,
			@RequestParam(required = false) final Double longitude) {

		EVoucherSearchBean searchDTO = new EVoucherSearchBean();
		searchDTO.setQuery(query);
		searchDTO.setLatitude(latitude);
		searchDTO.setLongitude(longitude);

		return ResponseEntity.ok(eVoucherProcessService.findEVouchersTemplates(searchDTO));
	}

	/**
	 * Find an eVoucher by its id
	 * 
	 * @param id Identifier of the eVoucher
	 * @return an {@link EVoucherObject}
	 */
	@GetMapping(value = "/evouchers/{id}", produces = { DEFAULT_API_CONTENT_TYPE_V1 })
	public ResponseEntity<EVoucherObject> findEVoucherById(@PathVariable Long id) {
		Optional<EVoucherObject> optEVoucher = eVoucherProcessService.findEVoucherById(id);
		EVoucherObject eVoucher = optEVoucher.orElseThrow(()-> new EVoucherNotFoundException(id, ""));
		return ResponseEntity.ok(eVoucher);
	}

	/**
	 * Create an new eVoucher.
	 * 
	 * @param bean eVoucher information
	 * @return The saved eVoucher object.
	 */
	@PostMapping(value = "/evouchers/", produces = { DEFAULT_API_CONTENT_TYPE_V1 })
	public ResponseEntity<EVoucherObject> createEVoucher(@Valid @RequestBody EVoucherSaveBean bean) {
		return ResponseEntity.ok(eVoucherProcessService.createEVoucher(bean));
	}
	
	/**
	 * Create an new eVoucher, print and send it.
	 * 
	 * @param bean eVoucher information
	 * @return The saved eVoucher object.
	 */
	@PostMapping(value = "/evouchers/process", produces = { DEFAULT_API_CONTENT_TYPE_V1 })
	public ResponseEntity<EVoucherObject> createAndProcessEVoucher(@Valid @RequestBody EVoucherSaveBean bean,
			@RequestParam(required = false, defaultValue = "") Set<@Email String> to,
			@RequestParam(required = false, defaultValue = "") Set<@Email String> cc) {
		return ResponseEntity.ok(eVoucherProcessService.processEVoucher(bean, to, cc));
	}
	/**
	 * Create an new eVoucher, print and send it.
	 * 
	 * @param bean eVoucher information
	 * @return Flux of eVoucher events.
	 */
	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EVoucherEventObject.class)))))
	@PostMapping(value = "/evouchers/process", produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<EVoucherEventObject> createAndProcessEVoucherWithEvents(@Valid @RequestBody EVoucherSaveBean bean,
			@RequestParam(required = false, defaultValue = "") Set<@Email String> to,
			@RequestParam(required = false, defaultValue = "") Set<@Email String> cc) {
		return eVoucherProcessService.processEVoucherWithEvents(bean, to, cc);
	}

	/**
	 * Save an existing eVoucher.
	 * 
	 * @param id   Identifier of the eVoucher to save.
	 * @param bean eVoucher information
	 * @return The saved eVoucher object.
	 */
	@PutMapping(value = "/evouchers/{id}", produces = { DEFAULT_API_CONTENT_TYPE_V1 })
	public ResponseEntity<EVoucherObject> updateEVoucher(@PathVariable Long id,
			@Valid @RequestBody EVoucherSaveBean bean) {
		return ResponseEntity.ok(eVoucherProcessService.updateEVoucher(id, bean));
	}

	/**
	 * Save an existing eVoucher.
	 * 
	 * @param id   Identifier of the eVoucher to save.
	 * @param bean eVoucher information
	 * @return The saved eVoucher object.
	 */
	@PutMapping(value = "/evouchers/{id}/status", produces = { DEFAULT_API_CONTENT_TYPE_V1 })
	public ResponseEntity<EVoucherObject> updateEVoucherStatus(@PathVariable Long id,
			@Valid @RequestBody EVoucherStatusBean bean) {
		EVoucherObject eVoucher = null;
		switch (bean.getStatus()) {
		case ARCHIVED -> eVoucher = eVoucherProcessService.archiveEVoucher(id);
		case SENT -> eVoucher = eVoucherProcessService.restoreEVoucher(id);
		default -> throw new InvalidEVoucherStatusParameterException(id, bean.getStatus(),
				Set.of(EVoucherStatus.ARCHIVED, EVoucherStatus.SENT));
		}
		return ResponseEntity.ok(eVoucher);
	}

	/**
	 * Delete an eVoucher by id.
	 * 
	 * @param id Identifier of the eVoucher to delete.
	 * @return Void response entity
	 */
	@DeleteMapping(value = "/evouchers/{id}")
	public ResponseEntity<Void> deleteEVoucherById(@PathVariable Long id) {
		eVoucherProcessService.deleteEVoucher(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Get eVoucher print.
	 * 
	 * @param uuid id of the eVoucher to print.
	 * @param refresh To force refreshing the preview.
	 * @return The print as a PDF
	 */
	@GetMapping(value = "/evouchers/{id}/preview", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<ByteArrayResource> previewEVoucherPrint(@PathVariable Long id,
			@RequestParam(required = false) final Boolean refresh) {
		boolean isForcedPrinting = refresh != null && refresh.booleanValue();
		EVoucherPrintObject print = eVoucherProcessService.previewEVoucher(id, isForcedPrinting);
		ByteArrayResource resource = new ByteArrayResource(print.getData());
		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.setContentType(MediaType.asMediaType(print.getMimeType()));
		respHeaders.setContentLength(resource.contentLength());
		respHeaders.set("Content-disposition", "inline; filename=" + print.getFilename());
		return ResponseEntity.ok().headers(respHeaders).body(resource);
	}

	/**
	 * Print and dispatch an eVoucher.
	 * 
	 * @param id identifier of the eVoucher
	 * @return Flux of eVoucher events.
	 */
	@Operation(responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EVoucherEventObject.class)))))
	@PostMapping(value = "/evouchers/{id}/dispatch", produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<EVoucherEventObject> printAndSendEvoucher(@PathVariable Long id,
			@RequestParam(required = false, defaultValue = "") Set<@Email String> to,
			@RequestParam(required = false, defaultValue = "") Set<@Email String> cc) {
		return eVoucherProcessService.dispatchEVoucher(id, to, cc);
	}
	
	/**
	 * Validate the signature of the eVoucher.
	 * 
	 * @param bean The validation bean
	 * @return a {@link EVoucherValidationResultObject}
	 */
	@PostMapping(value = "/evouchers/validation", produces = DEFAULT_API_CONTENT_TYPE_V1)
	public ResponseEntity<EVoucherValidationResultObject> validateEvoucher(@Valid @RequestBody EVoucherValidationBean bean) {
		return ResponseEntity.ok(eVoucherProcessService.validateEVoucherPrint(bean.getSignature()));
	}
	

}
