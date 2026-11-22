package com.rafael.simulator.scoring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RiskScorerTest {

    private final RiskScorer scorer = new RiskScorer();

    @Test
    void negativeUrgentWithRiskKeywordIsHigh() {
        // 2 (negative) + 2 (high priority) + 2 (keyword) = 6
        assertEquals("high", scorer.calculateRisk("Vou falar com o meu advogado", "negative", "high"));
    }

    @Test
    void negativeWithMediumPriorityIsMedium() {
        // 2 + 1 = 3
        assertEquals("medium", scorer.calculateRisk("Estou insatisfeito", "negative", "medium"));
    }

    @Test
    void neutralLowPriorityIsLow() {
        assertEquals("low", scorer.calculateRisk("Bom dia", "neutral", "low"));
    }
}
