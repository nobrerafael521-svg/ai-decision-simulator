package com.rafael.simulator.dto;

import com.rafael.simulator.model.AnalysisRecord;
import java.time.Instant;
import java.util.List;

public record AnalysisResponse(Long id, String text, String sentiment, String riskLevel, String priority,
                               String category, String recommendedAction, List<String> explanation,
                               Instant createdAt) {

    public static AnalysisResponse from(AnalysisRecord r) {
        return new AnalysisResponse(r.getId(), r.getText(), r.getSentiment(), r.getRiskLevel(), r.getPriority(),
                r.getCategory(), r.getRecommendedAction(), List.copyOf(r.getExplanation()), r.getCreatedAt());
    }
}
