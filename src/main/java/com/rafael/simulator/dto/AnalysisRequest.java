package com.rafael.simulator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnalysisRequest(
        @NotBlank(message = "text must not be blank")
        @Size(max = 2000, message = "text must have at most 2000 characters")
        String text) {
}
