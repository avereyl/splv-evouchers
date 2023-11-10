package org.splv.evouchers.core.tech.impl.search;

import static org.splv.evouchers.core.Constants.ADDRESS_SEARCH_FIELD;
import static org.splv.evouchers.core.Constants.DISTRIBUTION_YEAR_SEARCH_FIELD;
import static org.splv.evouchers.core.Constants.LOCATION_SEARCH_FIELD;
import static org.splv.evouchers.core.Constants.NAME_SEARCH_FIELD;

import java.util.List;
import java.util.StringTokenizer;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.spatial.DistanceUnit;
import org.hibernate.search.engine.spatial.GeoPoint;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.tech.EVoucherSearchingService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EVoucherSearchingServiceLucene implements EVoucherSearchingService {

	private final EntityManager entityManager;

	@Override
	public List<EVoucher> search(String query, int recordsNumber) {
		if (StringUtils.isBlank(query)) {
			return List.of();
		}
		SearchSession searchSession = Search.session(entityManager);
		StringTokenizer st = new StringTokenizer(query);

		SearchResult<EVoucher> results = searchSession.search(EVoucher.class).where(f -> f.bool().with(b -> {
			b.minimumShouldMatchNumber(1);
			while (st.hasMoreTokens()) {
				String token = st.nextToken() + "*";
				b.should(f.simpleQueryString().field(NAME_SEARCH_FIELD).boost(2.0f).field(ADDRESS_SEARCH_FIELD).matching(token));
			}

		})).sort(f -> f.score().then().field(DISTRIBUTION_YEAR_SEARCH_FIELD).desc()).fetch(recordsNumber);
		return results.hits();
	}

	@Override
	public List<EVoucher> search(Double latitude, Double longitude, Integer distance, int recordsNumber) {
		SearchSession searchSession = Search.session(entityManager);
		GeoPoint center = GeoPoint.of(latitude, longitude);
		SearchResult<EVoucher> results = searchSession.search(EVoucher.class)
				.where(f -> f.spatial().within().field(LOCATION_SEARCH_FIELD).circle(center, distance, DistanceUnit.METERS))
				.sort( f -> f.distance( LOCATION_SEARCH_FIELD, center ) )
				.fetch(recordsNumber);
		return results.hits();
	}

	public void indexEVouchers() {
		SearchSession searchSession = Search.session(entityManager);
		searchSession.massIndexer().start().thenRun(() -> log.info("EVoucher mass indexing succeeded!"))
				.exceptionally(throwable -> {
					log.error("EVoucher mass indexing failed!", throwable);
					return null;
				});
	}

}
