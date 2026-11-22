package com.rafael.simulator.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analyses", indexes = {
        @Index(name = "idx_analyses_risk_level", columnList = "risk_level"),
        @Index(name = "idx_analyses_created_at", columnList = "created_at")
})
public class AnalysisRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false, length = 16)
    private String sentiment;

    @Column(name = "risk_level", nullable = false, length = 16)
    private String riskLevel;

    @Column(nullable = false, length = 16)
    private String priority;

    @Column(nullable = false, length = 32)
    private String category;

    @Column(name = "recommended_action", nullable = false, length = 32)
    private String recommendedAction;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "analysis_explanations", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "reason", nullable = false)
    private List<String> explanation = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected AnalysisRecord() {
    }

    public AnalysisRecord(String text, String sentiment, String riskLevel, String priority,
                          String category, String recommendedAction, List<String> explanation) {
        this.text = text;
        this.sentiment = sentiment;
        this.riskLevel = riskLevel;
        this.priority = priority;
        this.category = category;
        this.recommendedAction = recommendedAction;
        this.explanation = new ArrayList<>(explanation);
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public String getSentiment() { return sentiment; }
    public String getRiskLevel() { return riskLevel; }
    public String getPriority() { return priority; }
    public String getCategory() { return category; }
    public String getRecommendedAction() { return recommendedAction; }
    public List<String> getExplanation() { return explanation; }
    public Instant getCreatedAt() { return createdAt; }
}
