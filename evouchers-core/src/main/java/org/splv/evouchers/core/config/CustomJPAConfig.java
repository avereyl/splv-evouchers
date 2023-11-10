package org.splv.evouchers.core.config;

import org.splv.evouchers.core.domain.EntityScanRoot;
import org.splv.evouchers.core.repository.RepositoryScanRoot;
import org.splv.evouchers.core.repository.support.ExtendedSimpleJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Declare {@link ExtendedSimpleJpaRepository} as base class for repository.
 * 
 * @author AVEREYL
 *
 */
@Configuration
@EntityScan(basePackageClasses = EntityScanRoot.class)// because not in same hierarchy as main application
@EnableJpaRepositories(basePackageClasses = RepositoryScanRoot.class, repositoryBaseClass = ExtendedSimpleJpaRepository.class)
public class CustomJPAConfig {

}
