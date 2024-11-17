package com.shixin.business.service;

import java.util.List;

import com.shixin.business.domain.UserExpand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.shixin.business.domain.Repair;
import com.shixin.business.domain.Stuinfo;

public interface StuServiceI {
    Stuinfo findById(String id);

    Page<Stuinfo> findStus(Pageable pageable, UserExpand user, String searchId, String searchDormName);

    Boolean importExcel(MultipartFile file);

    Boolean resetPwd(String stuId);

    Boolean del(String stuId);

    Boolean update(Stuinfo stuinfo);

    Boolean leaveSchool(List<String> ids);

    Boolean updatePhone(Stuinfo stuinfo);

    Boolean updatePwd(String id, String oldPwd, String newPwd);

    Page<Repair> findRepair(Pageable pageable, String userId);

    Boolean delRepairInfo(String repairId);

    Boolean createRepair(Repair repair, Stuinfo stuinfo);

    Page<Repair> findRepairHistory(Pageable pageable, String id);

    List<Stuinfo> findAll(UserExpand user);

    List<Stuinfo> getStus(String dormid);
}
