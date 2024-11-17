package com.shixin.business.service;

import com.shixin.business.domain.*;
import com.shixin.business.domain.vo.ExportScoreEntity;
import com.shixin.business.domain.vo.ExportScoreHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScoreServiceI {

    List<ExportScoreEntity> createScore(Staffinfo staffinfo, String name);

    Page<ScoreExtend> getScoreList(Pageable pageable, Staffinfo staffinfo);

    void updateScore(Score score);

    void importExcel(MultipartFile file) throws Exception;

    Boolean isCreateScore(Staffinfo staffinfo);

    void push(Staffinfo staffinfo);

    List<Scoringbatch> getScoreNames(UserExpand userExpand);

    Page<ScoreExtend> historyList(Pageable pageable, UserExpand userExpand, String scoringBatchId);

    List<ExportScoreHistoryEntity> findHistory(Staffinfo staffinfo);
}
