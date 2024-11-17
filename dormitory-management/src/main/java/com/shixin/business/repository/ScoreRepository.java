package com.shixin.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shixin.business.domain.Score;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, String>, JpaSpecificationExecutor<Score> {
    List<Score> findAllByStatusAndDormidLike(String status,String dormid);

    List<Score> findAllByStatusAndDormid(String status,String dormid);
}
