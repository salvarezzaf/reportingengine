package com.jpmc.reporting.stubs;

import com.jpmc.reporting.engine.TradeOperationsProvider;
import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TradeOperationsProviderStub implements TradeOperationsProvider{
    @Override
    public LocalDate calculateSettlementDate(LocalDate instructionDate, Currency currency) {
        System.out.println("calculateSettlementDate method called");
        return LocalDate.now();
    }

    @Override
    public Map<LocalDate, BigDecimal> calculateDailySettledAmount(List<Instruction> instructions, Operation op) {
        System.out.println("calculateDailySettledAmount method called");
        return new LinkedHashMap<>();
    }

    @Override
    public BigDecimal calculateInstructionTradeAmount(Instruction instruction) {
        System.out.println("calculateInstructionTradeAmount method called");
        return new BigDecimal("1.5");
    }

    @Override
    public Map<String, BigDecimal> rankEntitiesByInstructionAmount(List<Instruction> instructions, Operation op) {
        System.out.println("rankEntitiesByInstructionAmount method called");
        return new LinkedHashMap<>();
    }
}
