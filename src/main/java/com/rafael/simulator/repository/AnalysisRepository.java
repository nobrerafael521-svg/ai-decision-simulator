package com.rafael.simulator.repository;

import com.rafael.simulator.model.AnalysisRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisRepository extends JpaRepository<AnalysisRecord, Long> {

    Page<AnalysisRecord> findByRiskLevelIgnoreCase(String riskLevel, Pageable pageable);
}
