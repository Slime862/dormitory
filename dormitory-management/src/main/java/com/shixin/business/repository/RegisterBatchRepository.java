package com.shixin.business.repository;

import com.shixin.business.domain.Registerbatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisterBatchRepository extends JpaRepository<Registerbatch, String> {
    List<Registerbatch> findAllByStaffidAndStatus(String staffId, String status);
}
