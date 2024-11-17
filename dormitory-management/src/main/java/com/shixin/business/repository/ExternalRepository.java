package com.shixin.business.repository;

import com.shixin.business.domain.External;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExternalRepository extends JpaRepository<External, String>, JpaSpecificationExecutor<External> {
}
