package com.shixin.business.repository;

import com.shixin.business.domain.OutRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OutRegisterRepository extends JpaRepository<OutRegister, String>, JpaSpecificationExecutor<OutRegister> {
    List<OutRegister> findAllByStatusIn(String... status);

    OutRegister findByStuidAndStatus(String stuid, String status);

    @Modifying
    @Transactional
    @Query("update OutRegister inre set inre.status = :status where inre.status = '1' and inre.staffid = :staffid")
    int updateStatus(@Param("staffid") String staffid, @Param("status") String status);
}
