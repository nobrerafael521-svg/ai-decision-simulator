package com.rafael.simulator.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TextAnalyzerTest {

    private final TextAnalyzer analyzer = new TextAnalyzer();

    @ParameterizedTest
    @CsvSource({
            "Estou muito insatisfeito com o serviço, negative",
            "Quero fazer uma reclamação, negative",
            "Obrigado pela ajuda excelente, positive",
            "O serviço é bom, positive",
            "Qual é o horário de funcionamento?, neutral"
    })
    void detectsSentiment(String text, String expected) {
        assertEquals(expected, analyzer.detectSentiment(text));
    }

    @ParameterizedTest
    @CsvSource({
            "Quero cancelar a subscrição, complaint",
            "Qual o preço do plano anual?, sales",
            "Preciso de ajuda com um erro, support",
            "Bom dia, general"
    })
    void detectsCategory(String text, String expected) {
        assertEquals(expected, analyzer.detectCategory(text));
    }

    @ParameterizedTest
    @CsvSource({
            "Preciso disto urgente, high",
            "Resolvam já por favor, high",
            "Respondam quando possível, low",
            "Gostaria de mais informação, medium"
    })
    void detectsPriority(String text, String expected) {
        assertEquals(expected, analyzer.detectPriority(text));
    }

    @Test
    void urgencyUsesWholeWordMatching() {
        // "ja" inside "janela" and "mau" inside "maunual" must not trigger anything
        assertEquals("medium", analyzer.detectPriority("A janela do escritório está partida"));
        assertEquals("neutral", analyzer.detectSentiment("Consultei o manual de utilizador"));
    }
}
