package com.rafael.simulator.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rafael.simulator.explain.ExplanationBuilder;
import com.rafael.simulator.nlp.TextAnalyzer;
import com.rafael.simulator.scoring.RiskScorer;
import org.junit.jupiter.api.Test;

class DecisionEngineTest {

    private final TextAnalyzer analyzer = new TextAnalyzer();
    private final DecisionEngine engine =
            new DecisionEngine(analyzer, new RiskScorer(), new ExplanationBuilder(analyzer));

    @Test
    void urgentCancellationIsEscalatedToHuman() {
        Decision d = engine.process("Quero cancelar urgente, vou falar com o advogado");

        assertEquals("high", d.riskLevel());
        assertEquals("escalate_to_human", d.recommendedAction());
        assertTrue(d.explanation().contains("keyword detected: cancelar"));
        assertTrue(d.explanation().contains("urgent language detected"));
    }

    @Test
    void calmComplaintIsReviewed() {
        // negative (2) + medium priority (1) = 3 -> medium risk, no escalation
        Decision d = engine.process("Estou insatisfeito com a fatura");

        assertEquals("medium", d.riskLevel());
        assertEquals("complaint", d.category());
        assertEquals("review_case", d.recommendedAction());
    }

    @Test
    void neutralQuestionGetsAutoReply() {
        Decision d = engine.process("Qual o horário de funcionamento");

        assertEquals("auto_reply", d.recommendedAction());
    }
}
