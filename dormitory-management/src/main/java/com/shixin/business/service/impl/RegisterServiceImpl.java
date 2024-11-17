package com.shixin.business.service.impl;

import com.shixin.business.domain.*;
import com.shixin.business.domain.vo.ExportInRegisterEntity;
import com.shixin.business.domain.vo.ExportOutRegisterEntity;
import com.shixin.business.domain.vo.InRegisterExtend;
import com.shixin.business.domain.vo.OutRegisterExtend;
import com.shixin.business.repository.InRegisterRepository;
import com.shixin.business.repository.OutRegisterRepository;
import com.shixin.business.repository.RegisterBatchRepository;
import com.shixin.business.repository.StuRepository;
import com.shixin.business.service.RegisterServiceI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegisterServiceImpl implements RegisterServiceI {

    @Autowired
    private InRegisterRepository inRegisterRepository;

    @Autowired
    private RegisterBatchRepository registerbatchRepository;

    @Autowired
    private StuRepository stuRepository;

    @Autowired
    private OutRegisterRepository outRegisterRepository;

    @Override
    public Boolean isCreateRegister() {
        // 查询是否存在未登记完的
        List<InRegister> registers = inRegisterRepository.findAllByStatusIn("0", "1");
        if (registers != null && registers.size() <= 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void createRegister(String name, Staffinfo staffinfo) {
        List<Stuinfo> stuinfos = stuRepository.findAllByStatusAndDormidIsLike(0,
                staffinfo.getDormname() + staffinfo.getDormno() + "%");
        if (stuinfos != null && stuinfos.size() > 0) {
            Registerbatch registerbatch = registerbatchRepository.save(new Registerbatch(name, staffinfo.getId(), "in"));

            List<InRegister> inRegisters = stuinfos.stream().map(e -> {
                InRegister register = new InRegister();
                register.setStuid(e.getId());
                register.setStaffid(staffinfo.getId());
                register.setRebatchid(registerbatch.getId());
                register.setStatus("0");
                return register;
            }).collect(Collectors.toList());

            inRegisterRepository.saveAll(inRegisters);
        }
    }

    @Override
    public List<ExportInRegisterEntity> findInHistoryList(Staffinfo staffinfo) {
        List<InRegister> list = inRegisterRepository.findAll(createExportSpe(staffinfo.getId()), new Sort(Sort.DEFAULT_DIRECTION, "rebatchid"));
        return list.stream().map(e -> {
            ExportInRegisterEntity entity = new ExportInRegisterEntity();
            createEntity(entity, e.getStuid(), e.getRebatchid());
            entity.setArrivetime(e.getArrivetime());
            return entity;
        }).collect(Collectors.toList());
    }

    private Specification createExportSpe(String staffId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.equal(root.get("status"), "2");
            Predicate predicate2 = criteriaBuilder.equal(root.get("staffid"), staffId);
            return criteriaBuilder.and(predicate1, predicate2);
        };
    }

    @Override
    public List<Registerbatch> getRegisterNames(String staffId, String status) {
        String batchId = null;
        List<Registerbatch> list = null;
        if (StringUtils.equals(status,"in")) {
            List<InRegister> registers = inRegisterRepository.findAllByStatusIn("0", "1");
            if (registers != null && registers.size() > 0) {
                batchId = registers.get(0).getRebatchid();
            }
        } else {
            List<OutRegister> registers = outRegisterRepository.findAllByStatusIn("0", "1");
            if (registers != null && registers.size() > 0) {
                batchId = registers.get(0).getRebatchid();
            }
        }

        list = registerbatchRepository.findAllByStaffidAndStatus(staffId, status);
        if (batchId == null) {
            return list;
        } else {
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(batchId)) {
                        list.remove(i);
                        break;
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Page<InRegisterExtend> inRegisterList(Pageable pageable, Staffinfo staffinfo) {
        Specification<InRegister> registerSpec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.notEqual(root.get("status"), "2");
            Predicate predicate2 = criteriaBuilder.equal(root.get("staffid"), staffinfo.getId());
            return criteriaBuilder.and(predicate1, predicate2);
        };
        return createInRegisterExtend(registerSpec, pageable);
    }

    @Override
    public Page<InRegisterExtend> historyList(Pageable pageable, Staffinfo staffinfo, String registerBatchId) {
        if (StringUtils.isEmpty(registerBatchId)) {
            return null;
        }
        Specification<InRegister> registerSpec = createSpecification(registerBatchId, staffinfo.getId());
        return createInRegisterExtend(registerSpec, pageable);
    }

    @Override
    public Boolean createInHistory(Staffinfo staffinfo) {
        try {
            inRegisterRepository.updateStatus(staffinfo.getId(), "2");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Page<InRegisterExtend> createInRegisterExtend(Specification<InRegister> registerSpec, Pageable pageable) {
        Page<InRegister> inRegisters = inRegisterRepository.findAll(registerSpec, pageable);
        List<InRegisterExtend> extendList = inRegisters.getContent().stream().map(e -> {
            InRegisterExtend extend = new InRegisterExtend();
            createEntity(extend, e.getStuid(), e.getRebatchid());
            extend.setArrivetime(e.getArrivetime());
            return extend;
        }).collect(Collectors.toList());
        return new PageImpl<>(extendList, pageable, inRegisters.getTotalElements());
    }

    private Specification createSpecification(String registerBatchId, String staffId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.equal(root.get("status"), "2");
            Predicate predicate2 = criteriaBuilder.equal(root.get("rebatchid"), registerBatchId);
            Predicate predicate3 = criteriaBuilder.equal(root.get("staffid"), staffId);
            return criteriaBuilder.and(predicate1, predicate2, predicate3);
        };
    }

    @Override
    public InRegister getInRegisterId(String id) {
        return inRegisterRepository.findByStuidAndStatus(id, "0");
    }

    @Override
    public void saveStuRegister(Date arrivetime, String inRegisterId) {
        Optional<InRegister> register = inRegisterRepository.findById(inRegisterId);
        if (register.isPresent()) {
            InRegister inRegister = register.get();
            inRegister.setArrivetime(arrivetime);
            inRegister.setStatus("1");
            inRegisterRepository.save(inRegister);
        }
    }

    @Override
    public Boolean isStuSaveRegister(String stuId) {
        InRegister register = inRegisterRepository.findByStuidAndStatus(stuId, "0");
        if (register != null) {
            return true;
        }
        return false;
    }

    @Override
    public Page<InRegisterExtend> stuHistoryList(Pageable pageable, String stuId) {
        Specification<InRegister> registerSpec = createStuSpecification(stuId);

        Page<InRegister> registers = inRegisterRepository.findAll(registerSpec, pageable);

        List<InRegisterExtend> extendList = registers.getContent().stream().map(e -> {
            InRegisterExtend extend = new InRegisterExtend();
            BeanUtils.copyProperties(e, extend);

            Optional<Registerbatch> registerbatch = registerbatchRepository.findById(e.getRebatchid());
            if (registerbatch.isPresent()) {
                extend.setRegisterBatchName(registerbatch.get().getName());
            }
            return extend;
        }).collect(Collectors.toList());

        return new PageImpl<>(extendList, pageable, registers.getTotalElements());
    }

    private Specification createStuSpecification(String stuId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.equal(root.get("status"), "2");
            Predicate predicate2 = criteriaBuilder.equal(root.get("stuid"), stuId);
            return criteriaBuilder.and(predicate1, predicate2);
        };
    }

    @Override
    public Boolean isCreateOutRegister() {
        // 查询是否存在未登记完的
        List<OutRegister> registers = outRegisterRepository.findAllByStatusIn("0", "1");
        if (registers != null && registers.size() <= 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void createOutRegister(String name, Staffinfo staffinfo) {
        List<Stuinfo> stuinfos = stuRepository.findAllByStatusAndDormidIsLike(0,
                staffinfo.getDormname() + staffinfo.getDormno() + "%");
        if (stuinfos != null && stuinfos.size() > 0) {
            Registerbatch registerbatch = registerbatchRepository.save(new Registerbatch(name, staffinfo.getId(), "out"));

            List<OutRegister> inRegisters = stuinfos.stream().map(e -> {
                OutRegister register = new OutRegister();
                register.setStuid(e.getId());
                register.setStaffid(staffinfo.getId());
                register.setRebatchid(registerbatch.getId());
                register.setStatus("0");
                return register;
            }).collect(Collectors.toList());

            outRegisterRepository.saveAll(inRegisters);
        }
    }

    @Override
    public Page<OutRegisterExtend> outHistoryList(Pageable pageable, Staffinfo staffinfo, String registerBatchId) {
        if (registerBatchId == null || "".equals(registerBatchId)) {
            return null;
        }
        Specification<OutRegister> registerSpec = createSpecification(registerBatchId, staffinfo.getId());
        return createOutRegisterExtend(registerSpec, pageable);
    }

    @Override
    public Page<OutRegisterExtend> outRegisterList(Pageable pageable, Staffinfo staffinfo) {
        Specification<OutRegister> registerSpec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.notEqual(root.get("status"), "2");
            Predicate predicate2 = criteriaBuilder.equal(root.get("staffid"), staffinfo.getId());
            return criteriaBuilder.and(predicate1, predicate2);
        };
        return createOutRegisterExtend(registerSpec, pageable);
    }

    @Override
    public Boolean createOutHistory(Staffinfo staffinfo) {
        try {
            outRegisterRepository.updateStatus(staffinfo.getId(), "2");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Page<OutRegisterExtend> createOutRegisterExtend(Specification<OutRegister> registerSpec, Pageable pageable) {
        Page<OutRegister> outRegisters = outRegisterRepository.findAll(registerSpec, pageable);
        List<OutRegisterExtend> extendList = outRegisters.getContent().stream().map(e -> {
            OutRegisterExtend extend = new OutRegisterExtend();
            createEntity(extend, e.getStuid(), e.getRebatchid());
            extend.setLeavetime(e.getLeavetime());
            extend.setCity(e.getCity());
            return extend;
        }).collect(Collectors.toList());
        return new PageImpl<>(extendList, pageable, outRegisters.getTotalElements());
    }

    @Override
    public OutRegister getOutRegisterId(String id) {
        return outRegisterRepository.findByStuidAndStatus(id, "0");
    }

    @Override
    public Boolean isStuSaveOutRegister(String stuId) {
        OutRegister register = outRegisterRepository.findByStuidAndStatus(stuId, "0");
        if (register != null) {
            return true;
        }
        return false;
    }

    @Override
    public void saveStuOutRegister(Date leavetime, String outRegisterId, String city) {
        Optional<OutRegister> register = outRegisterRepository.findById(outRegisterId);
        if (register.isPresent()) {
            OutRegister outRegister = register.get();
            outRegister.setLeavetime(leavetime);
            outRegister.setCity(city);
            outRegister.setStatus("1");
            outRegisterRepository.save(outRegister);
        }
    }

    @Override
    public Page<OutRegisterExtend> stuOutHistoryList(Pageable pageable, String stuId) {
        Specification<OutRegister> registerSpec = createStuSpecification(stuId);

        Page<OutRegister> registers = outRegisterRepository.findAll(registerSpec, pageable);

        List<OutRegisterExtend> extendList = registers.getContent().stream().map(e -> {
            OutRegisterExtend extend = new OutRegisterExtend();
            BeanUtils.copyProperties(e, extend);

            Optional<Registerbatch> registerbatch = registerbatchRepository.findById(e.getRebatchid());
            if (registerbatch.isPresent()) {
                extend.setRegisterBatchName(registerbatch.get().getName());
            }
            return extend;
        }).collect(Collectors.toList());

        return new PageImpl<>(extendList, pageable, registers.getTotalElements());
    }

    @Override
    public List<ExportOutRegisterEntity> findOutHistoryList(Staffinfo staffinfo) {
        List<OutRegister> list = outRegisterRepository.findAll(createExportSpe(staffinfo.getId()), new Sort(Sort.DEFAULT_DIRECTION, "rebatchid"));
        return list.stream().map(e -> {
            ExportOutRegisterEntity entity = new ExportOutRegisterEntity();
            createEntity(entity, e.getStuid(), e.getRebatchid());
            entity.setLeavetime(e.getLeavetime());
            entity.setCity(e.getCity());
            return entity;
        }).collect(Collectors.toList());
    }

    /**
     * 创建对象
     *
     * @param o         对象
     * @param stuId     学生id
     * @param rebatchId rebatchId
     */
    private void createEntity(Object o, String stuId, String rebatchId) {
        Optional<Stuinfo> stuinfo = stuRepository.findById(stuId);
        Class c = o.getClass();
        if (stuinfo.isPresent()) {
            try {
                Stuinfo s = stuinfo.get();
                BeanUtils.copyProperties(s, o);
                Field f = c.getDeclaredField("stuId");
                f.setAccessible(true);
                f.set(o, s.getId());

                f = c.getDeclaredField("stuName");
                f.setAccessible(true);
                f.set(o, s.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Optional<Registerbatch> registerbatch = registerbatchRepository.findById(rebatchId);
        if (registerbatch.isPresent()) {
            try {
                Field f = c.getDeclaredField("registerBatchName");
                f.setAccessible(true);
                f.set(o, registerbatch.get().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
