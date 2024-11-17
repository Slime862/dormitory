package com.shixin.business.service.impl;

import com.shixin.business.domain.External;
import com.shixin.business.domain.Staffinfo;
import com.shixin.business.repository.ExternalRepository;
import com.shixin.business.service.ExternalServiceI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalServiceImpl implements ExternalServiceI {

    @Autowired
    private ExternalRepository externalRepository;

    @Override
    public void saveExternal(External external, Staffinfo staffinfo) {
        external.setVisittime(new Date());
        external.setDormname(staffinfo.getDormname() + staffinfo.getDormno());
        externalRepository.save(external);
    }

    @Override
    public Page<External> externalList(Pageable pageable, Staffinfo staffinfo) {
        Specification<External> externalSpec =
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicate1 = criteriaBuilder.isNull(root.get("endtime"));
                    Predicate predicate2 = criteriaBuilder.equal(root.get("dormname"),
                            staffinfo.getDormname() + staffinfo.getDormno());
                    return criteriaBuilder.and(predicate1, predicate2);
                };
        return externalRepository.findAll(externalSpec, pageable);
    }

    @Override
    public void overVisit(String id) {
        Optional<External> optional = externalRepository.findById(id);
        if (optional.isPresent()) {
            External external = optional.get();
            external.setEndtime(new Date());
            externalRepository.save(external);
        }
    }

    @Override
    public Page<External> externalHistoryList(Pageable pageable, Staffinfo staffinfo, String stuname, String startTime, String endTime) {
        List<Predicate> predicates = new ArrayList<>();
        Specification<External> externalSpec = (root, criteriaQuery, criteriaBuilder) -> {
            predicates.add(criteriaBuilder.isNotNull(root.get("endtime")));
            predicates.add(criteriaBuilder.equal(root.get("dormname"),
                    staffinfo.getDormname() + staffinfo.getDormno()));

            if (StringUtils.isNotBlank(stuname)) {
                predicates.add(criteriaBuilder.like(root.get("stuname"), stuname));
            }
            if (StringUtils.isNotBlank(startTime)) {
                //大于或等于传入时间
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("visittime").as(String.class), startTime));
            }
            if (StringUtils.isNotBlank(endTime)) {
                //小于或等于传入时间
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("visittime").as(String.class), endTime));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return externalRepository.findAll(externalSpec, pageable);
    }

    @Override
    public List<External> findHistoryList(Staffinfo staffinfo) {
        Specification<External> externalSpec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.isNotNull(root.get("endtime"));
            Predicate predicate2 = criteriaBuilder.equal(root.get("dormname"), staffinfo.getDormname() + staffinfo.getDormno());
            return criteriaBuilder.and(predicate1, predicate2);
        };
        return externalRepository.findAll(externalSpec);
    }
}
