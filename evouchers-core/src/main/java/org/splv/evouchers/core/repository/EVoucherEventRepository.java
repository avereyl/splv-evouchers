package org.splv.evouchers.core.repository;

import org.splv.evouchers.core.domain.EVoucherEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EVoucherEventRepository extends JpaRepository<EVoucherEvent, Long> {

}
