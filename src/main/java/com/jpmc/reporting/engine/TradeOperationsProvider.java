package com.jpmc.reporting.engine;

import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface TradeOperationsProvider {

    LocalDate calculateSettlementDate(LocalDate instructionDate, Currency currency);

    Map<LocalDate, BigDecimal> calculateDailySettledAmount(List<Instruction> instructions, Operation op);

    BigDecimal calculateInstructionTradeAmount(Instruction instruction);

}
