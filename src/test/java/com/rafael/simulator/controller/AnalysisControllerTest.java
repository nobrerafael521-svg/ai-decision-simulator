package com.rafael.simulator.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rafael.simulator.repository.AnalysisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalysisRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private String json(String text) {
        return "{\"text\": \"" + text + "\"}";
    }

    @Test
    void analyzeReturnsExplainedDecisionAndPersistsIt() throws Exception {
        mockMvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON)
                        .content(json("Quero cancelar urgente, vou falar com o advogado")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.riskLevel", is("high")))
                .andExpect(jsonPath("$.recommendedAction", is("escalate_to_human")))
                .andExpect(jsonPath("$.explanation", hasItem("keyword detected: cancelar")));

        mockMvc.perform(get("/api/analyses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].category", is("complaint")));
    }

    @Test
    void blankTextIsRejectedWithValidationErrors() throws Exception {
        mockMvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON).content(json("   ")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.text").exists());
    }

    @Test
    void textLongerThanLimitIsRejected() throws Exception {
        mockMvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON)
                        .content(json("a".repeat(2001))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listCanBeFilteredByRiskLevel() throws Exception {
        mockMvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON)
                .content(json("Quero cancelar urgente, vou falar com o advogado")));
        mockMvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON)
                .content(json("Qual o horário de funcionamento")));

        mockMvc.perform(get("/api/analyses").param("riskLevel", "high"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].riskLevel", is("high")));
    }

    @Test
    void getByIdReturns404WhenMissing() throws Exception {
        mockMvc.perform(get("/api/analyses/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is("Analysis 999999 not found")));
    }

    @Test
    void getByIdReturnsStoredAnalysis() throws Exception {
        String body = mockMvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON)
                        .content(json("Preciso de ajuda com um erro")))
                .andReturn().getResponse().getContentAsString();
        String id = body.replaceAll(".*\"id\":(\\d+).*", "$1");

        mockMvc.perform(get("/api/analyses/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category", is("support")));
    }
}
