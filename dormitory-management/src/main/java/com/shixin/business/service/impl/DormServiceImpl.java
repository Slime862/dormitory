package com.shixin.business.service.impl;

import java.util.List;

import com.shixin.business.domain.Staffinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shixin.business.domain.Dorm;
import com.shixin.business.repository.DormRepository;
import com.shixin.business.service.DormServiceI;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Service
public class DormServiceImpl implements DormServiceI {

    @Autowired
    private DormRepository dormRepository;

    @Override
    public Page<Dorm> findDorms(Pageable pageable) {
        return dormRepository.findAll(pageable);
    }

    @Override
    public Boolean importExcel(MultipartFile file) {
        try {
            List<Dorm> list = ExcelImportUtil.importExcel(file.getInputStream(), Dorm.class, new ImportParams());
            list.stream().forEach(e -> e.setId(e.getDormname() + e.getDormno() + "-" + e.getRoomno()));
            dormRepository.saveAll(list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Dorm> getDorms(Staffinfo staffinfo) {
        return dormRepository.findByDormnameAndDormno(staffinfo.getDormname(), staffinfo.getDormno());
    }
}
