package org.splv.evouchers.core.tech.impl.search;

import org.hibernate.search.mapper.pojo.bridge.RoutingBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.RoutingBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.RoutingBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.RoutingBridgeRouteContext;
import org.hibernate.search.mapper.pojo.route.DocumentRoutes;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucher_;

public class EVoucherStatusRoutingBinder implements RoutingBinder {

	@Override
	public void bind(RoutingBindingContext context) {
		context.dependencies().use(EVoucher_.INDEXING_STRATEGY);
		context.bridge(EVoucher.class, new EVoucherIndexingBridge());
	}
	
	public static class EVoucherIndexingBridge implements RoutingBridge<EVoucher> {

		@Override
		public void route(DocumentRoutes routes, Object entityIdentifier, EVoucher indexedEntity,
				RoutingBridgeRouteContext context) {
			switch (indexedEntity.getIndexingStrategy()) {
			case INDEX -> routes.addRoute();
			case IGNORE -> routes.notIndexed();
			default -> throw new IllegalArgumentException("Unexpected value: " + indexedEntity.getIndexingStrategy());
			}
		}

		@Override
		public void previousRoutes(DocumentRoutes routes, Object entityIdentifier, EVoucher indexedEntity,
				RoutingBridgeRouteContext context) {
			routes.addRoute();
		}

	}

}
