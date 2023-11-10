package org.splv.evouchers.core.tech;

import java.util.List;

import org.splv.evouchers.core.domain.EVoucher;

public interface EVoucherSearchingService {

	/**
	 * Search for eVoucher's.
	 * 
	 * @param query         The query
	 * @param recordsNumber Number max of records to return
	 * @return a list of eVouchers
	 */
	List<EVoucher> search(final String query, int recordsNumber);

	/**
	 * Search for eVoucher's.
	 * 
	 * @param latitude      Latitude
	 * @param longitude     Longitude
	 * @param distance      Distance in meters
	 * @param recordsNumber Number max of records to return
	 * @return a list of eVouchers
	 */
	List<EVoucher> search(final Double latitude, final Double longitude, final Integer distance, int recordsNumber);

	/**
	 * Index all eVouchers
	 */
	void indexEVouchers();
}
