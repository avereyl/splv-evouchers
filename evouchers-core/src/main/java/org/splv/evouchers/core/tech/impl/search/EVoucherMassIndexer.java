package org.splv.evouchers.core.tech.impl.search;

import org.splv.evouchers.core.tech.EVoucherSearchingService;
import org.springframework.beans.factory.InitializingBean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EVoucherMassIndexer implements InitializingBean {

	private final EVoucherSearchingService eVoucherSearchingService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		eVoucherSearchingService.indexEVouchers();
	}

}
