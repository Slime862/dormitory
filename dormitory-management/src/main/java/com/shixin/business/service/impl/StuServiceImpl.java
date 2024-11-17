package com.shixin.business.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.shixin.business.domain.*;
import com.shixin.business.repository.StuRepository;
import com.shixin.business.repository.UserRepository;
import com.shixin.business.service.RepairServiceI;
import com.shixin.business.service.StuServiceI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StuServiceImpl implements StuServiceI {

    @Autowired
    private StuRepository stuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepairServiceI repairServiceI;

    @Override
    public Stuinfo findById(String id) {
        return stuRepository.findById(id).get();
    }

    @Override
    public List<Stuinfo> findAll(UserExpand user) {
        if (user.getStaffinfo() != null) {
            Specification specification = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("dormid"), user.getStaffinfo().getDormname() + user.getStaffinfo().getDormno() + "%");
            return stuRepository.findAll(specification);
        } else {
            return stuRepository.findAll();
        }
    }

    @Override
    public Page<Stuinfo> findStus(Pageable pageable, UserExpand user, String searchId, String searchDormName) {
        if (user.getStaffinfo() != null) {
            return stuRepository.findAll(createSpe(searchId, searchDormName, user.getStaffinfo()), pageable);
        } else {
            return stuRepository.findAll(createSpe(searchId, searchDormName, null), pageable);
        }
    }

    public Specification createSpe(String searchId, String searchDormName, Staffinfo staffinfo) {
        List<Predicate> predicates = new ArrayList<>();
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (StringUtils.isNotEmpty(searchId)) {
                predicates.add(criteriaBuilder.equal(root.get("id"), searchId));
            }
            if (StringUtils.isNotEmpty(searchDormName)) {
                predicates.add(criteriaBuilder.equal(root.get("grade"), searchDormName));
            }
            if (null != staffinfo) {
                predicates.add(criteriaBuilder.like(root.get("dormid"),
                        staffinfo.getDormname() + staffinfo.getDormno() + "%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("status"), 0));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


    @Override
    @Transactional
    public Boolean importExcel(MultipartFile file) {
        try {
            List<Stuinfo> stuList = ExcelImportUtil.importExcel(file.getInputStream(), Stuinfo.class,
                    new ImportParams());
            stuRepository.saveAll(stuList);
            List<User> userList = stuList.stream().map(e -> {
                User user = new User();
                user.setUsername(e.getId());
                user.setPassword("123456");
                user.setPermission(0);
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
    public Boolean leaveSchool(List<String> ids) {
        try {
            stuRepository.update(1, ids);
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean resetPwd(String stuId) {
        try {
            User user = userRepository.findByUsername(stuId);
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
    public Boolean del(String stuId) {
        try {
            Stuinfo stuinfo = new Stuinfo();
            stuinfo.setId(stuId);
            stuRepository.delete(stuinfo);
            User user = userRepository.findByUsername(stuId);
            userRepository.delete(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean update(Stuinfo stuinfo) {
        try {
            stuRepository.save(stuinfo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean updatePhone(Stuinfo stuinfo) {
        try {
            stuRepository.updatePhone(stuinfo.getId(), stuinfo.getPhone());
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
            u.setPermission(0);
            userRepository.save(u);
            return true;
        }
        return false;
    }

    @Override
    public Page<Repair> findRepair(Pageable pageable, String userId) {
        return repairServiceI.findRepairNotEqualStatusAndEqualUserId(pageable, userId, 2);
    }

    @Override
    public Boolean createRepair(Repair repair, Stuinfo stuinfo) {
        try {
            repair.setStatus(0);
            repair.setDate(new Date());
            repair.setDormid(stuinfo.getDormid());
            repair.setStuid(stuinfo.getId());
            repairServiceI.createRepair(repair);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean delRepairInfo(String repairId) {
        try {
            repairServiceI.delRepair(repairId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Page<Repair> findRepairHistory(Pageable pageable, String id) {
        return repairServiceI.findRepairHistory(pageable, id);
    }

    @Override
    public List<Stuinfo> getStus(String dormid) {
        return stuRepository.findByDormid(dormid);
    }
}
