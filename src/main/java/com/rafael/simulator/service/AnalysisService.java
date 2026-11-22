package com.rafael.simulator.service;

import com.rafael.simulator.dto.AnalysisRequest;
import com.rafael.simulator.dto.AnalysisResponse;
import com.rafael.simulator.engine.Decision;
import com.rafael.simulator.engine.DecisionEngine;
import com.rafael.simulator.exception.AnalysisNotFoundException;
import com.rafael.simulator.model.AnalysisRecord;
import com.rafael.simulator.repository.AnalysisRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisService {

    private final DecisionEngine decisionEngine;
    private final AnalysisRepository repository;

    public AnalysisService(DecisionEngine decisionEngine, AnalysisRepository repository) {
        this.decisionEngine = decisionEngine;
        this.repository = repository;
    }

    @Transactional
    public AnalysisResponse analyze(AnalysisRequest request) {
        String text = request.text().strip();
        Decision d = decisionEngine.process(text);
        AnalysisRecord saved = repository.save(new AnalysisRecord(text, d.sentiment(), d.riskLevel(),
                d.priority(), d.category(), d.recommendedAction(), d.explanation()));
        return AnalysisResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<AnalysisResponse> list(String riskLevel, Pageable pageable) {
        Page<AnalysisRecord> page = (riskLevel == null || riskLevel.isBlank())
                ? repository.findAll(pageable)
                : repository.findByRiskLevelIgnoreCase(riskLevel, pageable);
        return page.map(AnalysisResponse::from);
    }

    @Transactional(readOnly = true)
    public AnalysisResponse get(Long id) {
        return repository.findById(id).map(AnalysisResponse::from)
                .orElseThrow(() -> new AnalysisNotFoundException(id));
    }
}
