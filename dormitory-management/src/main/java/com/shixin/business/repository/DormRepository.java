package com.shixin.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shixin.business.domain.Dorm;

import java.util.List;

@Repository
public interface DormRepository extends JpaRepository<Dorm, String> {
    List<Dorm> findByDormnameAndDormno(String dormname, String dormno);
}
