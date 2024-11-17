package com.shixin.business.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.shixin.business.domain.Repair;
import com.shixin.business.domain.Staffinfo;

import java.util.List;

public interface StaffServiceI {
    Staffinfo findById(String id);

    Page<Staffinfo> findStaffs(Pageable pageable, String searchName, String searchDorm);

    Boolean importExcel(MultipartFile file);

    Boolean resetPwd(String staffId);

    Boolean del(String staffId);

    Boolean update(Staffinfo staffinfo);

    Boolean updatePhone(Staffinfo staffinfo);

    Boolean updatePwd(String id, String oldPwd, String newPwd);

    Page<Repair> findRepairUntreated(Pageable pageable, Staffinfo staffinfo, String reason);

    Page<Repair> findRepairProcessInfo(Pageable pageable, Staffinfo staffinfo);

    Boolean dealRepairUntreated(String repairId);

    Boolean dealRepairProcess(String repairId);

    Page<Repair> findRepairHistory(Pageable pageable, String reason, Staffinfo staffinfo);

    Boolean batchDeal(List<String> ids);
}
