package com.shixin.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.shixin.business.repository.RepairRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shixin.business.domain.Repair;
import com.shixin.business.domain.Staffinfo;
import com.shixin.business.domain.User;
import com.shixin.business.repository.StaffRepository;
import com.shixin.business.repository.UserRepository;
import com.shixin.business.service.RepairServiceI;
import com.shixin.business.service.StaffServiceI;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import javax.persistence.criteria.Predicate;

@Service
public class StaffServiceImpl implements StaffServiceI {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepairServiceI repairServiceI;

    @Autowired
    private RepairRepository repairRepository;

    @Override
    public Staffinfo findById(String id) {
        return staffRepository.findById(id).get();
    }

    @Override
    public Page<Staffinfo> findStaffs(Pageable pageable, String searchName, String searchDorm) {
        List<Staffinfo> list = new ArrayList<>();
        String[] strs = null;
        Staffinfo staffinfo = null;
        if (StringUtils.isNotEmpty(searchName) || StringUtils.isNotEmpty(searchDorm)) {
            if (StringUtils.contains(searchDorm, "-")) {
                strs = StringUtils.split("-");
            }
            if (null == strs) {
                staffinfo = staffRepository.findByStaffnameLike("%" + searchName + "%");
            } else {
                staffinfo = staffRepository.findByStaffnameAndDormnameAndDormno(searchName, strs[0], strs[1]);
            }
            list.add(staffinfo);
            return new PageImpl<>(list);
        }
        return staffRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Boolean importExcel(MultipartFile file) {
        try {
            List<Staffinfo> staffList = ExcelImportUtil.importExcel(file.getInputStream(), Staffinfo.class,
                    new ImportParams());
            staffRepository.saveAll(staffList);
            List<User> userList = staffList.stream().map(e -> {
                User user = new User();
                user.setUsername(e.getId());
                user.setPassword("123456");
                user.setPermission(1);
                return user;
            }).collect(Collectors.toList());
            userRepository.saveAll(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean resetPwd(String staffId) {
        try {
            User user = userRepository.findByUsername(staffId);
            user.setPassword("123456");
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean del(String staffId) {
        try {
            Staffinfo staffinfo = new Staffinfo();
            staffinfo.setId(staffId);
            staffRepository.delete(staffinfo);
            User user = userRepository.findByUsername(staffId);
            userRepository.delete(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean update(Staffinfo staffinfo) {
        try {
            Optional<Staffinfo> optional = staffRepository.findById(staffinfo.getId());
            if (!optional.isPresent()) {
                User user = new User();
                user.setUsername(staffinfo.getId());
                user.setPassword("123456");
                user.setPermission(1);
                userRepository.save(user);
            }
            staffRepository.save(staffinfo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean updatePhone(Staffinfo staffinfo) {
        try {
            staffRepository.updatePhone(staffinfo.getId(), staffinfo.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean updatePwd(String id, String oldPwd, String newPwd) {
        User user = userRepository.findByUsername(id);
        if (null != user && StringUtils.equals(user.getPassword(), oldPwd)) {
            User u = new User();
            u.setId(user.getId());
            u.setUsername(id);
            u.setPassword(newPwd);
            u.setPermission(1);
            userRepository.save(u);
            return true;
        }
        return false;
    }

    @Override
    public Page<Repair> findRepairUntreated(Pageable pageable, Staffinfo staffinfo, String reason) {
        if (StringUtils.contains(reason, "all")) {
            return repairServiceI.findRepairLikeDormNameEqualStatus(pageable, staffinfo.getDormname() + staffinfo.getDormno(), 0);
        } else {
            Specification<Repair> spec = (root, criteriaQuery, criteriaBuilder) -> {
                Predicate predicate1 = criteriaBuilder.equal(root.get("status"), 0);
                Predicate predicate2 = criteriaBuilder.equal(root.get("reason"), reason);
                Predicate predicate3 = criteriaBuilder.like(root.get("dormid"), staffinfo.getDormname() + staffinfo.getDormno() + "%");
                return criteriaBuilder.and(predicate1, predicate2, predicate3);
            };
            return repairRepository.findAll(spec, pageable);
        }
    }

    @Override
    public Boolean dealRepairUntreated(String repairId) {
        try {
            repairServiceI.updateRepairStatus(repairId, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Page<Repair> findRepairProcessInfo(Pageable pageable, Staffinfo staffinfo) {
        return repairServiceI.findRepairLikeDormNameEqualStatus(pageable, staffinfo.getDormname() + staffinfo.getDormno(), 1);
    }

    @Override
    public Boolean dealRepairProcess(String repairId) {
        try {
            repairServiceI.updateRepairStatus(repairId, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Page<Repair> findRepairHistory(Pageable pageable, String reason, Staffinfo staffinfo) {
        if (StringUtils.contains(reason, "all")) {
            return repairServiceI.findRepairLikeDormNameEqualStatus(pageable, staffinfo.getDormname() + staffinfo.getDormno(), 2);
        } else {
            Specification<Repair> spec = (root, criteriaQuery, criteriaBuilder) -> {
                Predicate predicate1 = criteriaBuilder.equal(root.get("status"), 2);
                Predicate predicate2 = criteriaBuilder.equal(root.get("reason"), reason);
                Predicate predicate3 = criteriaBuilder.like(root.get("dormid"),
                        staffinfo.getDormname() + staffinfo.getDormno() + "%");
                return criteriaBuilder.and(predicate1, predicate2, predicate3);
            };
            return repairRepository.findAll(spec, pageable);
        }
    }


    @Override
    public Boolean batchDeal(List<String> ids) {
        try {
            ids.stream().forEach(e -> repairServiceI.updateRepairStatus(e, 1));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
