package com.shixin.business.repository;

import com.shixin.business.domain.InRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InRegisterRepository extends JpaRepository<InRegister, String>, JpaSpecificationExecutor<InRegister> {

    List<InRegister> findAllByStatusIn(String... status);

    InRegister findByStuidAndStatus(String stuid,String status);

    @Modifying
    @Transactional
    @Query("update InRegister inre set inre.status = :status where inre.status = '1' and inre.staffid = :staffid")
    int updateStatus(@Param("staffid") String staffid, @Param("status") String status);
}
