package com.rafael.simulator.engine;

import com.rafael.simulator.explain.ExplanationBuilder;
import com.rafael.simulator.nlp.TextAnalyzer;
import com.rafael.simulator.scoring.RiskScorer;
import org.springframework.stereotype.Component;

@Component
public class DecisionEngine {

    private final TextAnalyzer textAnalyzer;
    private final RiskScorer riskScorer;
    private final ExplanationBuilder explanationBuilder;

    public DecisionEngine(TextAnalyzer textAnalyzer, RiskScorer riskScorer, ExplanationBuilder explanationBuilder) {
        this.textAnalyzer = textAnalyzer;
        this.riskScorer = riskScorer;
        this.explanationBuilder = explanationBuilder;
    }

    public Decision process(String text) {
        String sentiment = textAnalyzer.detectSentiment(text);
        String category = textAnalyzer.detectCategory(text);
        String priority = textAnalyzer.detectPriority(text);
        String riskLevel = riskScorer.calculateRisk(text, sentiment, priority);
        String action = decideAction(riskLevel, priority, category);

        return new Decision(sentiment, riskLevel, priority, category, action,
                explanationBuilder.build(text, sentiment, category, priority, riskLevel));
    }

    private String decideAction(String riskLevel, String priority, String category) {
        if ("high".equalsIgnoreCase(riskLevel) || "high".equalsIgnoreCase(priority)) {
            return "escalate_to_human";
        }
        if ("complaint".equalsIgnoreCase(category)) {
            return "review_case";
        }
        return "auto_reply";
    }
}
