package com.jpmc.reporting.engine;

import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.*;

public class SimpleTradeOperationsProvider implements TradeOperationsProvider {

    @Override
    public LocalDate calculateSettlementDate(LocalDate instructionDate, Currency currency) {



        LocalDate settlementDate = instructionDate.plusDays(1);

        if (isWorkingDay(settlementDate, currency)) {
            return settlementDate;
        } else {
            while (!isWorkingDay(settlementDate, currency)) {
                settlementDate = settlementDate.plusDays(1);
            }
            return settlementDate;
        }
    }

    @Override
    public Map<LocalDate, BigDecimal> calculateDailySettledAmount(List<Instruction> instructions, Operation op) {

        if(instructions == null || instructions.isEmpty() || op == null)
            throw new IllegalArgumentException("Instructions and trade operation cannot be null");

        Set<Map.Entry<LocalDate, BigDecimal>> unsortedAmountSet = instructions.stream()
                .filter(instruction -> instruction.getTradeOperation().equals(op))
                .collect(groupingBy(filteredInstr -> this.calculateSettlementDate(filteredInstr.getInstructionDate(), filteredInstr.getCurrency()),
                        mapping(this::calculateInstructionTradeAmount,
                                reducing(BigDecimal.ZERO, BigDecimal::add)))).entrySet();

        return sortSettledAmountByDate(unsortedAmountSet);
    }

    @Override
    public BigDecimal calculateInstructionTradeAmount(Instruction instruction) {

        if(instruction == null)
            throw new IllegalArgumentException("Instruction cannot be null for trade amount calculation");

        return  instruction.getUnitPrice()
                .multiply(BigDecimal.valueOf(instruction.getUnits()))
                .multiply(instruction.getAgreedFx())
                .setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }


    private boolean isWorkingDay(LocalDate instructionDate, Currency currency) {
        EnumSet<DayOfWeek> workingWeek = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        EnumSet<DayOfWeek> altWorkingWeek = EnumSet.range(DayOfWeek.THURSDAY, DayOfWeek.SUNDAY);

        if ("AED".equals(currency.getCurrencyCode()) || "SAR".equals(currency.getCurrencyCode()))
            return altWorkingWeek.contains(instructionDate.getDayOfWeek());
        else
            return workingWeek.contains(instructionDate.getDayOfWeek());
    }

    private Map<LocalDate, BigDecimal> sortSettledAmountByDate(Set<Map.Entry<LocalDate, BigDecimal>> unsortedAmountSet) {

       return unsortedAmountSet.stream()
                .sorted(comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

    }

}
