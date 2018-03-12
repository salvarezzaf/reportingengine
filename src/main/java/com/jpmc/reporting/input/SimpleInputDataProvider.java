package com.jpmc.reporting.input;

import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SimpleInputDataProvider implements InputDataProvider {

    @Override
    public List<Instruction> retrieveInstructions() {

        List<Instruction> instructions = new ArrayList<>();

        Instruction instruction1 =
                new Instruction.Builder()
                        .entity("foo")
                        .tradeOperation(Operation.BUY)
                        .agreedFx(new BigDecimal(0.50))
                        .currency(Currency.getInstance("SGD"))
                        .instructionDate(LocalDate.of(2016, Month.JANUARY, 1))
                        .settlementDate(LocalDate.of(2016, Month.JANUARY, 5))
                        .units(200)
                        .unitPrice(new BigDecimal(100.25))
                        .build();

        Instruction instruction2 =
                new Instruction.Builder()
                        .entity("bar")
                        .tradeOperation(Operation.SELL)
                        .agreedFx(new BigDecimal(0.22))
                        .currency(Currency.getInstance("AED"))
                        .instructionDate(LocalDate.of(2016, Month.APRIL, 8))
                        .settlementDate(LocalDate.of(2016, Month.APRIL, 13))
                        .units(450)
                        .unitPrice(new BigDecimal(150.5))
                        .build();

        Instruction instruction3 =
                new Instruction.Builder()
                        .entity("abc")
                        .tradeOperation(Operation.SELL)
                        .agreedFx(new BigDecimal(0.38))
                        .currency(Currency.getInstance("AED"))
                        .instructionDate(LocalDate.of(2016, Month.SEPTEMBER, 8))
                        .settlementDate(LocalDate.of(2016, Month.SEPTEMBER, 17))
                        .units(220)
                        .unitPrice(new BigDecimal(124.5))
                        .build();

        Instruction instruction4 =
                new Instruction.Builder()
                        .entity("def")
                        .tradeOperation(Operation.BUY)
                        .agreedFx(new BigDecimal(0.75))
                        .currency(Currency.getInstance("SAR"))
                        .instructionDate(LocalDate.of(2016, Month.JUNE, 5))
                        .settlementDate(LocalDate.of(2016, Month.JUNE, 17))
                        .units(231)
                        .unitPrice(new BigDecimal(131.5))
                        .build();

        Instruction instruction5 =
                new Instruction.Builder()
                        .entity("xyz")
                        .tradeOperation(Operation.BUY)
                        .agreedFx(new BigDecimal(1.06))
                        .currency(Currency.getInstance("GBP"))
                        .instructionDate(LocalDate.of(2016, Month.JULY, 6))
                        .settlementDate(LocalDate.of(2016, Month.JULY, 19))
                        .units(571)
                        .unitPrice(new BigDecimal(110.5))
                        .build();

        Instruction instruction6 =
                new Instruction.Builder()
                        .entity("mac")
                        .tradeOperation(Operation.SELL)
                        .agreedFx(new BigDecimal(1.12))
                        .currency(Currency.getInstance("EUR"))
                        .instructionDate(LocalDate.of(2016, Month.OCTOBER, 15))
                        .settlementDate(LocalDate.of(2016, Month.OCTOBER, 19))
                        .units(171)
                        .unitPrice(new BigDecimal(103.5))
                        .build();

        instructions.add(instruction1);
        instructions.add(instruction2);
        instructions.add(instruction3);
        instructions.add(instruction4);
        instructions.add(instruction5);
        instructions.add(instruction6);

        return instructions;
    }
}
