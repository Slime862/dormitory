package com.shixin.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shixin.business.domain.Scoringbatch;

import java.util.List;

@Repository
public interface ScoringbatchRepository extends JpaRepository<Scoringbatch, String> {
    List<Scoringbatch> findAllByDormname(String dormname);
}
