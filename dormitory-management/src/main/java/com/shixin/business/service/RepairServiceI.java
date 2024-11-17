package com.shixin.business.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shixin.business.domain.Repair;

import java.util.List;

public interface RepairServiceI {

    Page<Repair> findRepairNotEqualStatusAndEqualUserId(Pageable pageable, String userId, Integer status);

    Page<Repair> findRepairLikeDormNameEqualStatus(Pageable pageable, String dormName, Integer status);

    Page<Repair> findRepairHistory(Pageable pageable, String userId);

    void createRepair(Repair repair);

    void updateRepairStatus(String repairId, Integer status);

    void delRepair(String repairId);

    List<Repair> findRepairLikeDormNameEqualStatus(String dormName, Integer status);

}
