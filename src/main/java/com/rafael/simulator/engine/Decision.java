package com.rafael.simulator.engine;

import java.util.List;

public record Decision(String sentiment, String riskLevel, String priority, String category,
                       String recommendedAction, List<String> explanation) {
}
