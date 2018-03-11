package com.jpmc.reporting.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class Instruction {

    private final String entity;
    private final Operation tradeOperation;
    private final BigDecimal agreedFx;
    private final Currency currency;
    private final LocalDate instructionDate;
    private LocalDate settlementDate;
    private final int units;
    private final BigDecimal unitPrice;

    public static class Builder {

        private String entity;
        private Operation tradeOperation;
        private BigDecimal agreedFx;
        private Currency currency;
        private LocalDate instructionDate;
        private LocalDate settlementDate;
        private int units;
        private BigDecimal unitPrice;

        public Builder entity(String val) {
            entity = val;
            return this;
        }

        public Builder tradeOperation(Operation val) {
            tradeOperation = val;
            return this;
        }

        public Builder agreedFx(BigDecimal val) {
            agreedFx = val;
            return this;
        }

        public Builder currency(Currency val) {
            currency = val;
            return this;
        }

        public Builder instructionDate(LocalDate val) {
            instructionDate = val;
            return this;
        }

        public Builder settlementDate(LocalDate val) {
            settlementDate = val;
            return this;
        }

        public Builder units(int val) {
            units = val;
            return this;
        }

        public Builder unitPrice(BigDecimal val) {
            unitPrice = val;
            return this;
        }

        public Instruction build() {
            return new Instruction(this);
        }
    }


    private Instruction(Builder builder) {

        entity = builder.entity;
        tradeOperation = builder.tradeOperation;
        agreedFx = builder.agreedFx;
        currency = builder.currency;
        instructionDate = builder.instructionDate;
        settlementDate = builder.settlementDate;
        units = builder.units;
        unitPrice = builder.unitPrice;
    }

    public String getEntity() {
        return entity;
    }

    public Operation getTradeOperation() {
        return tradeOperation;
    }

    public BigDecimal getAgreedFx() {
        return agreedFx;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDate getInstructionDate() {
        return instructionDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public int getUnits() {
        return units;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Override
    public String toString() {

        return "Entity: " + this.entity + ", Operation: " + this.tradeOperation +
                ", AgreedFx: " + this.agreedFx + ", Currency: " + this.currency +
                ", InstructionDate: " + this.instructionDate + ", SettlementDate: " + this.settlementDate +
                ", Units: " + this.units + ", unitPrice: " + this.unitPrice;
     }

}
