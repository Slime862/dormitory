package com.shixin.business.service.impl;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shixin.business.domain.Repair;
import com.shixin.business.repository.RepairRepository;
import com.shixin.business.service.RepairServiceI;

import java.util.List;

@Service
public class RepairServiceImpl implements RepairServiceI {

    @Autowired
    private RepairRepository repairRepository;

    @Override
    public Page<Repair> findRepairNotEqualStatusAndEqualUserId(Pageable pageable, String userId, Integer status) {
        Specification<Repair> spec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.notEqual(root.get("status"), status);
            Predicate p2 = criteriaBuilder.equal(root.get("stuid"), userId);
            return criteriaBuilder.and(p1, p2);
        };
        return repairRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Repair> findRepairLikeDormNameEqualStatus(Pageable pageable, String dormName, Integer status) {
        Specification<Repair> spec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.equal(root.get("status"), status);
            Predicate predicate2 = criteriaBuilder.like(root.get("dormid"), dormName + "%");
            return criteriaBuilder.and(predicate1, predicate2);
        };
        return repairRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Repair> findRepairHistory(Pageable pageable, String userId) {
        Specification<Repair> spec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.equal(root.get("status"), 2);
            Predicate p2 = criteriaBuilder.equal(root.get("stuid"), userId);
            return criteriaBuilder.and(p1, p2);
        };
        return repairRepository.findAll(spec, pageable);
    }

    @Override
    public void createRepair(Repair repair) {
        repairRepository.save(repair);
    }

    @Override
    public void updateRepairStatus(String repairId, Integer status) {
        Repair repair = repairRepository.findById(repairId).get();
        repair.setStatus(status);
        repairRepository.save(repair);
    }

    @Override
    public void delRepair(String repairId) {
        Repair repair = new Repair();
        repair.setId(repairId);
        repairRepository.delete(repair);
    }

    @Override
    public List<Repair> findRepairLikeDormNameEqualStatus(String dormName, Integer status) {
        Specification<Repair> spec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.equal(root.get("status"), status);
            Predicate p2 = criteriaBuilder.like(root.get("dormid"), dormName + "%");
            return criteriaBuilder.and(p1, p2);
        };
        return repairRepository.findAll(spec);
    }

}
