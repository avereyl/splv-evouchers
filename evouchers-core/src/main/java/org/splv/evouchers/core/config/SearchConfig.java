package org.splv.evouchers.core.config;

import org.splv.evouchers.core.tech.EVoucherSearchingService;
import org.splv.evouchers.core.tech.impl.search.EVoucherMassIndexer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "app.index-on-startup", havingValue = "true", matchIfMissing = true)
@Configuration
public class SearchConfig {

	@Bean
	EVoucherMassIndexer massIndexer(EVoucherSearchingService eVoucherSearchingService) {
		return new EVoucherMassIndexer(eVoucherSearchingService);
	}
}
