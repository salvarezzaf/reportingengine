package com.jpmc.reporting.util;

import com.jpmc.reporting.input.InputDataProvider;
import com.jpmc.reporting.input.SimpleInputDataProvider;
import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class TestDataUtil {

    private List<Instruction> instructions;
    private Map<LocalDate, BigDecimal> expectedSettledAmount;
    private Map<String, BigDecimal> expectedEntityRanking;

    public TestDataUtil() {
        this.instructions = new ArrayList<>();
        this.expectedSettledAmount = new LinkedHashMap<>();
        this.expectedEntityRanking = new LinkedHashMap<>();
    }

    public List<Instruction> getInstructionsData() {
        InputDataProvider dataProvider = new SimpleInputDataProvider();
        instructions = dataProvider.retrieveInstructions();

        return instructions;
    }

    public void addInstructionToTestData(String entity, Operation op, BigDecimal agreedFx, Currency fx, LocalDate inDate, int units, BigDecimal unitPrice) {
        Instruction instruction =
                new Instruction.Builder()
                        .entity(entity)
                        .tradeOperation(op)
                        .agreedFx(agreedFx)
                        .currency(fx)
                        .instructionDate(inDate)
                        .settlementDate(inDate)
                        .units(units)
                        .unitPrice(unitPrice)
                        .build();

        instructions.add(instruction);
    }

    public Map<LocalDate, BigDecimal> getExpectedSettleAmountForOp(Operation op) {
        if (op.equals(Operation.BUY)) {

            expectedSettledAmount.put(LocalDate.of(2016, Month.JANUARY, 4), new BigDecimal("10025.00"));
            expectedSettledAmount.put(LocalDate.of(2016, Month.JUNE, 9), new BigDecimal("22782.38"));
            expectedSettledAmount.put(LocalDate.of(2016, Month.JULY, 7), new BigDecimal("66881.23"));

            return new LinkedHashMap<>(expectedSettledAmount);
        } else {
            expectedSettledAmount.put(LocalDate.of(2016, Month.APRIL, 9), new BigDecimal("14899.50"));
            expectedSettledAmount.put(LocalDate.of(2016, Month.SEPTEMBER, 9), new BigDecimal("10408.20"));
            expectedSettledAmount.put(LocalDate.of(2016, Month.OCTOBER, 17), new BigDecimal("19822.32"));

            return new LinkedHashMap<>(expectedSettledAmount);
        }

    }

    public Map<String, BigDecimal> getExpectedEntityRankingForOp(Operation op, boolean isTie) {

        if (op.equals(Operation.BUY)) {
            expectedEntityRanking.put("xyz", new BigDecimal("66881.23"));
            if (isTie) { expectedEntityRanking.put("bmw", new BigDecimal("66881.23")); }
            expectedEntityRanking.put("def", new BigDecimal("22782.38"));
            expectedEntityRanking.put("foo", new BigDecimal("10025.00"));

            return new LinkedHashMap<>(expectedEntityRanking);
        } else {

            expectedEntityRanking.put("mac", new BigDecimal("19822.32"));
            if(isTie) { expectedEntityRanking.put("poc", new BigDecimal("19822.32"));}
            expectedEntityRanking.put("bar", new BigDecimal("14899.50"));
            expectedEntityRanking.put("abc", new BigDecimal("10408.20"));

            return new LinkedHashMap<>(expectedEntityRanking);
        }

    }


}
