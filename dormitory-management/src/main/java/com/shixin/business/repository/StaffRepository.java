package com.shixin.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shixin.business.domain.Staffinfo;

@Repository
public interface StaffRepository extends JpaRepository<Staffinfo, String> {

	Staffinfo findByStaffnameAndDormnameAndDormno(String staffname, String string, String string2);

	Staffinfo findByStaffnameLike(String string);

	@Modifying
	@Transactional
	@Query("update Staffinfo sta set sta.phone = :phone where sta.id = :id")
	int updatePhone(@Param("id") String id,@Param("phone") String phone);

}
