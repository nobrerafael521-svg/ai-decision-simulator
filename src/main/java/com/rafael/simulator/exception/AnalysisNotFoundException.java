package com.rafael.simulator.exception;

public class AnalysisNotFoundException extends RuntimeException {

    public AnalysisNotFoundException(Long id) {
        super("Analysis " + id + " not found");
    }
}
