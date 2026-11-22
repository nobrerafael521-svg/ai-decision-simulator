package com.rafael.simulator.explain;

import static com.rafael.simulator.util.TextMatcher.containsAny;

import com.rafael.simulator.nlp.TextAnalyzer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

/** Produces the human-readable reasons behind a decision. */
@Component
public class ExplanationBuilder {

    private final TextAnalyzer textAnalyzer;

    public ExplanationBuilder(TextAnalyzer textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
    }

    public List<String> build(String text, String sentiment, String category, String priority, String riskLevel) {
        List<String> reasons = new ArrayList<>();
        String t = text.toLowerCase(Locale.ROOT);

        reasons.add("sentiment detected: " + sentiment);
        reasons.add("category detected: " + category);
        reasons.add("priority detected: " + priority);
        reasons.add("risk calculated: " + riskLevel);

        if (t.contains("cancelar")) {
            reasons.add("keyword detected: cancelar");
        }
        if (textAnalyzer.isUrgent(t)) {
            reasons.add("urgent language detected");
        }
        if (containsAny(t, "reclamação", "reclamacao")) {
            reasons.add("complaint-related keyword detected");
        }
        if (containsAny(t, "problema", "erro")) {
            reasons.add("support issue keyword detected");
        }
        return reasons;
    }
}
