package org.splv.evouchers.core.process;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.splv.evouchers.core.process.beans.in.EVoucherFilterBean;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;
import org.splv.evouchers.core.process.beans.in.EVoucherSearchBean;
import org.splv.evouchers.core.process.beans.out.EVoucherEventObject;
import org.splv.evouchers.core.process.beans.out.EVoucherObject;
import org.splv.evouchers.core.process.beans.out.EVoucherPrintObject;
import org.splv.evouchers.core.process.beans.out.EVoucherValidationResultObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import reactor.core.publisher.Flux;

public interface EVoucherProcessService {

	/**
	 * Create an eVoucher.
	 * 
	 * @param eVoucherSaveBean creation bean
	 * @return a newly created {@link EVoucherObject}.
	 */
	EVoucherObject createEVoucher(EVoucherSaveBean eVoucherSaveBean);
	
	/**
	 * Read the eVoucher with given id, fails if not found.
	 * @param id The eVoucher identifier
	 * @return the {@link EVoucherObject}
	 */
	EVoucherObject readEVoucher(long id);
	
	/**
	 * Update the eVoucher with given identifier.
	 * @param id The eVoucher identifier
	 * @param eVoucherSaveBean update bean
	 * @return the updated {@link EVoucherObject}
	 */
	EVoucherObject updateEVoucher(long id, EVoucherSaveBean eVoucherSaveBean);
		
	/**
	 * Delete the eVoucher with given identifier.
	 * @param id The eVoucher identifier
	 */
	void deleteEVoucher(long id);
	
	/**
	 * Archive the eVoucher with given identifier.
	 * @param id The eVoucher identifier
	 * @return the archived {@link EVoucherObject}
	 */
	EVoucherObject archiveEVoucher(long id);
	
	/**
	 * Restore the eVoucher with given identifier.
	 * @param id The eVoucher identifier
	 * @return the archived {@link EVoucherObject}
	 */
	EVoucherObject restoreEVoucher(long id);
	
	/**
	 * Print the eVoucher with given identifier (Generate a PDF).
	 * @param id The eVoucher identifier
	 * @param isForcedPrinting To indicate if the printed has to be refreshed
	 * @return the dispatched {@link EVoucherPrintObject}
	 */
	EVoucherPrintObject previewEVoucher(long id, boolean isForcedPrinting);
	
	/**
	 * Dispatch eVoucher print with given identifier (Send it by mail).
	 * @param id The eVoucher identifier
	 * @param to List of mail address to send the mail to.
	 * @param cc List of mail address to send a copy of the mail.
	 * @return a {@link Flux} of {@link EVoucherEventObject}
	 */
	Flux<EVoucherEventObject> dispatchEVoucher(long id, Set<String> to, Set<String> cc);
	
	/**
	 * Process an eVoucher creation request, returns events on each steps.
	 * <ul>
	 * <li>Save eVoucher
	 * <li>Print eVoucher
	 * <li>Dispatch eVoucher
	 * </ul>
	 * 
	 * @param eVoucherSaveBean creation bean
	 * @param to List of mail address to send the mail to.
	 * @param cc List of mail address to send a copy of the mail.
	 * @return a {@link Flux} of {@link EVoucherEventObject}
	 */
	Flux<EVoucherEventObject> processEVoucherWithEvents(EVoucherSaveBean eVoucherSaveBean, Set<String> to,
			Set<String> cc);
	
	/**
	 * Process an eVoucher creation request.
	 * <ul>
	 * <li>Save eVoucher
	 * <li>Print eVoucher
	 * <li>Dispatch eVoucher
	 * </ul>
	 * 
	 * @param eVoucherSaveBean creation bean
	 * @param to List of mail address to send the mail to.
	 * @param cc List of mail address to send a copy of the mail.
	 * @return a {@link EVoucherObject}
	 */
	EVoucherObject processEVoucher(EVoucherSaveBean eVoucherSaveBean, Set<String> to, Set<String> cc);
	
	/**
	 * Find eVoucher with given id
	 * @param id The eVoucher identifier
	 * @return an {@link Optional} {@link EVoucherObject}
	 */
	Optional<EVoucherObject> findEVoucherById(long id);
	
	/**
	 * Find page of eVouchers complying with search information
	 * @param eVoucherFilterBean eVoucher filter bean
	 * @param pageable pagination data
	 * @return a {@link Page} of {@link EVoucherObject}
	 */
	Page<EVoucherObject> findEVouchers(EVoucherFilterBean eVoucherFilterBean, Pageable pageable);
	
	/**
	 * Find eVouchers template complying with search information.
	 * Gather donor information.
	 * @param eVoucherSearchBean
	 * @return {@link List} of filled {@link EVoucherSaveBean}s
	 */
	List<EVoucherSaveBean> findEVouchersTemplates(EVoucherSearchBean eVoucherSearchBean);
	
	/**
	 * Validate an eVoucher fingerprint
	 * @param fingerprint The fingerprint to validate
	 * @return the validation result
	 */
	EVoucherValidationResultObject validateEVoucherPrint(String fingerprint);
	
}
