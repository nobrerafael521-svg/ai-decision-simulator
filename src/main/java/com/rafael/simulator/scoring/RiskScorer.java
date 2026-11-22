package com.rafael.simulator.scoring;

import static com.rafael.simulator.util.TextMatcher.containsAny;

import java.util.Locale;
import org.springframework.stereotype.Component;

/** Additive risk score: negative sentiment (+2), priority (high +2, medium +1), risk keywords (+2). */
@Component
public class RiskScorer {

    public String calculateRisk(String text, String sentiment, String priority) {
        String t = text.toLowerCase(Locale.ROOT);
        int score = 0;

        if ("negative".equalsIgnoreCase(sentiment)) {
            score += 2;
        }
        if ("high".equalsIgnoreCase(priority)) {
            score += 2;
        } else if ("medium".equalsIgnoreCase(priority)) {
            score += 1;
        }
        if (containsAny(t, "cancelar", "advogado", "tribunal", "reclamação", "reclamacao", "fraude", "urgente")) {
            score += 2;
        }

        if (score >= 5) {
            return "high";
        }
        if (score >= 3) {
            return "medium";
        }
        return "low";
    }
}
