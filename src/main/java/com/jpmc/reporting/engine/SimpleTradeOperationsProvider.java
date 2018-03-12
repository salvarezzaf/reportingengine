package com.jpmc.reporting.engine;

import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.*;

public class SimpleTradeOperationsProvider implements TradeOperationsProvider {

    @Override
    public LocalDate calculateSettlementDate(LocalDate instructionDate, Currency currency) {
        if (instructionDate == null || currency == null)
            throw new IllegalArgumentException("A valid date and currency symbol are required for settlement date calculation");

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

        if (instructions == null || instructions.isEmpty() || op == null)
            throw new IllegalArgumentException("Instructions and operations must not be null/empty for amount settled calculation");

        return instructions.stream()
                .filter(instruction -> instruction.getTradeOperation().equals(op))
                .collect(groupingBy(filteredInstr -> this.calculateSettlementDate(filteredInstr.getInstructionDate(), filteredInstr.getCurrency()),
                        mapping(this::calculateInstructionTradeAmount, reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet().stream()
                .sorted(comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

    }

    @Override
    public BigDecimal calculateInstructionTradeAmount(Instruction instruction) {

        if (instruction == null)
            throw new IllegalArgumentException("Instruction must not be null for trade amount calculation");

        return instruction.getUnitPrice()
                .multiply(BigDecimal.valueOf(instruction.getUnits()))
                .multiply(instruction.getAgreedFx())
                .setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    @Override
    public Map<String, BigDecimal> rankEntitiesByInstructionAmount(List<Instruction> instructions, Operation op) {

        if (instructions == null || instructions.isEmpty() || op == null)
            throw new IllegalArgumentException("Instructions and operations must not be null/empty for amount settled calculation");

        return instructions.stream()
                .filter(instruction -> instruction.getTradeOperation().equals(op))
                .collect(groupingBy(Instruction::getEntity,
                        mapping(this::calculateInstructionTradeAmount, reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet().stream()
                .sorted(Map.Entry.<String,BigDecimal>comparingByValue().reversed())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
    }

    private Map<?,BigDecimal> calculateAndSort(List<Instruction> instructions, Operation op, String keyName,Comparator<Map.Entry<?,BigDecimal>> comp) {

       return instructions.stream()
                .filter(instruction -> instruction.getTradeOperation().equals(op))
                .collect(groupingBy(filteredInstr -> "entity".equals(keyName) ? filteredInstr.getEntity() :
                                this.calculateSettlementDate(filteredInstr.getInstructionDate(), filteredInstr.getCurrency()),
                        mapping(this::calculateInstructionTradeAmount, reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet().stream()
                .sorted(comp)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

    }

    private boolean isWorkingDay(LocalDate instructionDate, Currency currency) {
        EnumSet<DayOfWeek> workingWeek = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        EnumSet<DayOfWeek> altWorkingWeek = EnumSet.range(DayOfWeek.THURSDAY, DayOfWeek.SUNDAY);

        if ("AED".equals(currency.getCurrencyCode()) || "SAR".equals(currency.getCurrencyCode()))
            return altWorkingWeek.contains(instructionDate.getDayOfWeek());
        else
            return workingWeek.contains(instructionDate.getDayOfWeek());
    }


}
