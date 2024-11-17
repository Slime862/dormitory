package com.shixin.business.service;

import com.shixin.business.domain.*;
import com.shixin.business.domain.vo.ExportInRegisterEntity;
import com.shixin.business.domain.vo.ExportOutRegisterEntity;
import com.shixin.business.domain.vo.InRegisterExtend;
import com.shixin.business.domain.vo.OutRegisterExtend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface RegisterServiceI {
    Boolean isCreateRegister();

    void createRegister(String name, Staffinfo staffinfo);

    List<ExportInRegisterEntity> findInHistoryList(Staffinfo staffinfo);

    List<Registerbatch> getRegisterNames(String staffId, String status);

    Page<InRegisterExtend> inRegisterList(Pageable pageable, Staffinfo staffinfo);

    Page<InRegisterExtend> historyList(Pageable pageable, Staffinfo staffinfo, String registerBatchId);

    Boolean createInHistory(Staffinfo staffinfo);

    InRegister getInRegisterId(String id);

    void saveStuRegister(Date arrivetime, String inRegisterId);

    Page<InRegisterExtend> stuHistoryList(Pageable pageable, String stuId);

    Boolean isStuSaveRegister(String stuId);

    Boolean isCreateOutRegister();

    void createOutRegister(String name, Staffinfo staffinfo);

    Page<OutRegisterExtend> outHistoryList(Pageable pageable, Staffinfo staffinfo, String registerBatchId);

    Page<OutRegisterExtend> outRegisterList(Pageable pageable, Staffinfo staffinfo);

    Boolean createOutHistory(Staffinfo staffinfo);

    OutRegister getOutRegisterId(String id);

    Boolean isStuSaveOutRegister(String stuId);

    void saveStuOutRegister(Date leavetime, String outRegisterId, String city);

    Page<OutRegisterExtend> stuOutHistoryList(Pageable pageable, String stuId);

    List<ExportOutRegisterEntity> findOutHistoryList(Staffinfo staffinfo);
}
