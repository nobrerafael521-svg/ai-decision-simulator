package com.rafael.simulator.nlp;

import static com.rafael.simulator.util.TextMatcher.containsAny;
import static com.rafael.simulator.util.TextMatcher.containsWord;

import java.util.Locale;
import org.springframework.stereotype.Component;

/** Rule-based text analysis (Portuguese keywords). Deterministic, so every result is explainable. */
@Component
public class TextAnalyzer {

    public String detectSentiment(String text) {
        String t = text.toLowerCase(Locale.ROOT);

        if (containsAny(t, "insatisfeito", "reclamação", "reclamacao", "cancelar", "problema", "péssimo", "pessimo")
                || containsWord(t, "mau")) {
            return "negative";
        }
        if (containsAny(t, "obrigado", "excelente", "satisfeito") || containsWord(t, "bom")) {
            return "positive";
        }
        return "neutral";
    }

    public String detectCategory(String text) {
        String t = text.toLowerCase(Locale.ROOT);

        if (containsAny(t, "cancelar", "insatisfeito", "reclamação", "reclamacao")) {
            return "complaint";
        }
        if (containsAny(t, "comprar", "preço", "preco", "interessado")) {
            return "sales";
        }
        if (containsAny(t, "ajuda", "erro", "suporte", "problema")) {
            return "support";
        }
        return "general";
    }

    public String detectPriority(String text) {
        String t = text.toLowerCase(Locale.ROOT);

        if (isUrgent(t)) {
            return "high";
        }
        if (containsAny(t, "quando possível", "quando possivel", "depois")) {
            return "low";
        }
        return "medium";
    }

    public boolean isUrgent(String lowerCaseText) {
        return containsAny(lowerCaseText, "urgente", "imediatamente", "agora")
                || containsWord(lowerCaseText, "já")
                || containsWord(lowerCaseText, "ja");
    }
}
