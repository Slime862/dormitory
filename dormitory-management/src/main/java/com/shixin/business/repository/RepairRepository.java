package com.shixin.business.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shixin.business.domain.Repair;

@Repository
public interface RepairRepository extends CrudRepository<Repair, String>,JpaSpecificationExecutor<Repair> {
}
