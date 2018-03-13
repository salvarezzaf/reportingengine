package com.jpmc.reporting.model;

public enum Operation {

    BUY("outgoing"), SELL("incoming");

    private String operation;

    Operation(String operation) {
        this.operation = operation;
    }


    String getOperation() {
        return this.operation;
    }

}
