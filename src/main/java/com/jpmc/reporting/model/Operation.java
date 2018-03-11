package com.jpmc.reporting.model;

public enum Operation {

    BUY("B"), SELL("S");

    private String operation;

    Operation(String operation) {
        this.operation = operation;
    }


    String getOperation() {
        return this.operation;
    }

}
