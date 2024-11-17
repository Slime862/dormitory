package com.shixin.business.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.shixin.business.domain.*;
import com.shixin.business.domain.vo.ExportScoreEntity;
import com.shixin.business.domain.vo.ExportScoreHistoryEntity;
import com.shixin.business.repository.DormRepository;
import com.shixin.business.repository.ScoreRepository;
import com.shixin.business.repository.ScoringbatchRepository;
import com.shixin.business.repository.StuRepository;
import com.shixin.business.service.ScoreServiceI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements ScoreServiceI {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ScoringbatchRepository scoringbatchRepository;

    @Autowired
    private DormRepository dormRepository;

    @Autowired
    private StuRepository stuRepository;

    @Override
    @Transactional
    public List<ExportScoreEntity> createScore(Staffinfo staffinfo, String name) {
        // 1.保存评分批次
        Scoringbatch scoringbatch = scoringbatchRepository.save(new Scoringbatch().setName(name).setDormname(staffinfo.getDormname() + staffinfo.getDormno()));
        // 2.创建评分
//        List<Dorm> list = dormRepository.findByDormnameAndDormno(staffinfo.getDormname(), staffinfo.getDormno());

        List<String> ids = stuRepository.getDormIds(staffinfo.getDormname() + staffinfo.getDormno() + "%");

        List<Dorm> list = dormRepository.findAllById(ids);

        List<Score> entities = list.stream().map(e -> {
            Score score = new Score();
            score.setBatchid(scoringbatch.getId());
            score.setDormid(e.getId());
            score.setStatus("0");// 创建
            return score;
        }).collect(Collectors.toList());

        List<Score> scores = scoreRepository.saveAll(entities);

        List<ExportScoreEntity> exportScoreEntitys = scores.stream().map(e -> {
            ExportScoreEntity entity = new ExportScoreEntity();
            BeanUtils.copyProperties(e, entity);
            entity.setName(scoringbatch.getName());

            Stuinfo stuinfo = stuRepository.findDistinctByDormid(e.getDormid());
            if (null != stuinfo)
                entity.setAcademy(stuinfo.getAcademy());
            return entity;
        }).collect(Collectors.toList());

        return exportScoreEntitys;
    }

    @Override
    public Boolean isCreateScore(Staffinfo staffinfo) {
        // 查询是否存在未评分完的
        List<Score> scoreList = scoreRepository.findAllByStatusAndDormidLike("0",
                staffinfo.getDormname() + staffinfo.getDormno() + "%");
        if (scoreList != null && scoreList.size() <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void importExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<ExportScoreEntity> list = ExcelImportUtil.importExcel(file.getInputStream(), ExportScoreEntity.class, params);
        List<Score> scoreList = list.stream().map(e -> {
            Score score = new Score();
            BeanUtils.copyProperties(e, score);
            score.setStatus("1");// 创建完成
            return score;
        }).sorted(Comparator.comparingDouble(Score::getSum).reversed()).collect(Collectors.toList());

        for (int i = 0; i < list.size(); i++) {
            scoreList.get(i).setOrderscore(String.valueOf(i + 1));
        }
        scoreRepository.saveAll(scoreList);
    }

    @Override
    public Page<ScoreExtend> getScoreList(Pageable pageable, Staffinfo staffinfo) {
        Specification<Score> scoreSpec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.equal(root.get("status"), "1");
            Predicate predicate2 = criteriaBuilder.like(root.get("dormid"),
                    staffinfo.getDormname() + staffinfo.getDormno() + "%");
            return criteriaBuilder.and(predicate1, predicate2);
        };

        Page<Score> scores = scoreRepository.findAll(scoreSpec, pageable);

        List<ScoreExtend> list = createScoreExtend(scores);

        return new PageImpl<>(list, pageable, scores.getTotalElements());
    }

    @Override
    public void updateScore(Score score) {
        scoreRepository.save(score);
    }

    @Override
    public void push(Staffinfo staffinfo) {
        List<Score> scoreList = scoreRepository.findAllByStatusAndDormidLike("1",
                staffinfo.getDormname() + staffinfo.getDormno() + "%");
        List<Score> list = scoreList.stream().map(e -> e.setStatus("2")).collect(Collectors.toList());
        scoreRepository.saveAll(list);
    }

    @Override
    public List<Scoringbatch> getScoreNames(UserExpand userExpand) {
        String batchId = null;
        List<Score> scoreList;
        List<Scoringbatch> scoringbatchList;

        if (userExpand.getStaffinfo() != null) {
            Staffinfo staffinfo = userExpand.getStaffinfo();
            scoreList = scoreRepository.findAllByStatusAndDormidLike("0",
                    staffinfo.getDormname() + staffinfo.getDormno() + "%");
            scoringbatchList = scoringbatchRepository.findAllByDormname(staffinfo.getDormname() + staffinfo.getDormno());
        } else {
            Stuinfo stuinfo = userExpand.getStuinfo();
            scoreList = scoreRepository.findAllByStatusAndDormid("0", stuinfo.getDormid());
            scoringbatchList = scoringbatchRepository.findAllByDormname(stuinfo.getDormid().substring(0, 2));
        }

        if (scoreList != null && scoreList.size() > 0) {
            batchId = scoreList.get(0).getBatchid();
        }

        if (batchId != null) {
            if (scoringbatchList != null) {
                for (int i = 0; i < scoringbatchList.size(); i++) {
                    if (scoringbatchList.get(i).getId().equals(batchId)) {
                        scoringbatchList.remove(i);
                        break;
                    }
                }
            }
        }
        return scoringbatchList;
    }

    @Override
    public Page<ScoreExtend> historyList(Pageable pageable, UserExpand userExpand, String scoringBatchId) {
        Specification<Score> scoreSpec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.equal(root.get("status"), "2");
            Predicate predicate2 = criteriaBuilder.equal(root.get("batchid"), scoringBatchId);
            Predicate predicate3;
            if (userExpand.getStaffinfo() != null) {
                predicate3 = criteriaBuilder.like(root.get("dormid"), userExpand.getStaffinfo().getDormname() + "%");
            } else {
                predicate3 = criteriaBuilder.like(root.get("dormid"), userExpand.getStuinfo().getDormid().substring(0, 1) + "%");
            }
            return criteriaBuilder.and(predicate1, predicate2, predicate3);
        };


        Page<Score> scores = scoreRepository.findAll(scoreSpec, pageable);

        List<ScoreExtend> list = createScoreExtend(scores);

        return new PageImpl<>(list, pageable, scores.getTotalElements());
    }

    @Override
    public List<ExportScoreHistoryEntity> findHistory(Staffinfo staffinfo) {
        Specification<Score> scoreSpec = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate1 = criteriaBuilder.equal(root.get("status"), "2");
            Predicate predicate2 = criteriaBuilder.like(root.get("dormid"), staffinfo.getDormname() + "%");
            return criteriaBuilder.and(predicate1, predicate2);
        };
        return scoreRepository.findAll(scoreSpec, new Sort(Sort.DEFAULT_DIRECTION, "batchid","orderscore")).stream().map(e -> {
            ExportScoreHistoryEntity entity = new ExportScoreHistoryEntity();
            BeanUtils.copyProperties(e, entity);
            Stuinfo stuinfo = stuRepository.findDistinctByDormid(e.getDormid());
            if (null != stuinfo)
                entity.setAcademy(stuinfo.getAcademy());

            Optional<Scoringbatch> optional = scoringbatchRepository.findById(e.getBatchid());
            if (optional.isPresent()) {
                entity.setScoringbatchName(optional.get().getName());
            }
            return entity;
        }).collect(Collectors.toList());
    }

    private List<ScoreExtend> createScoreExtend(Page<Score> scores) {
        return scores.stream().map(e -> {
            ScoreExtend scoreExtend = new ScoreExtend();
            BeanUtils.copyProperties(e, scoreExtend);

            Stuinfo stuinfo = stuRepository.findDistinctByDormid(e.getDormid());
            if (null != stuinfo)
                scoreExtend.setAcademy(stuinfo.getAcademy());
            return scoreExtend;
        }).collect(Collectors.toList());
    }
}
