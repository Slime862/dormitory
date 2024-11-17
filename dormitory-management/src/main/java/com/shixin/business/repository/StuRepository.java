package com.shixin.business.repository;

import java.util.List;

import com.shixin.business.domain.Dorm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shixin.business.domain.Stuinfo;

@Repository
public interface StuRepository extends JpaRepository<Stuinfo, String>, JpaSpecificationExecutor<Stuinfo> {

    @Modifying
    @Transactional
    @Query("update Stuinfo stu set stu.status = :status where stu.id in (:ids)")
    int update(@Param("status") Integer status, @Param("ids") List<String> ids);

    @Modifying
    @Transactional
    @Query("update Stuinfo stu set stu.phone = :phone where stu.id = :id")
    int updatePhone(@Param("id") String id, @Param("phone") String phone);

    Stuinfo findDistinctByDormid(String dormid);

    List<Stuinfo> findByDormid(String dormid);

    List<Stuinfo> findAllByStatusAndDormidIsLike(Integer status, String dormId);

    @Query("select distinct dormid from Stuinfo stu where stu.dormid like :dormId AND stu.status = 0")
    List<String> getDormIds(String dormId);
}
