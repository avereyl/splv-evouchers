package org.splv.evouchers.core.repository.support;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityManagerAwareJpaSpecificationExecutor<T>
		extends JpaSpecificationExecutor<T> {

	EntityManager getEntityManager();

	<S extends T> S detach(S entity);
	
	<S extends T> S refresh(S entity);

	void clear2ndLevelCache();

}
