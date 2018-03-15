package com.jpmc.reporting.engine;

import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface TradeOperationsProvider {
    /**
     * Calculates effective settlement date from instruction creation date. Instructions are aways settled
     * in a working day unless they fall outside the working week. In that case, the next working day is
     * set as settlement date for the instruction. Currency type is used to identify standard working week
     * days for the target trade market
     *
     * @param instructionDate - date of creation of instruction
     * @param currency - currency type for instruction
     *
     * @return - Effective settlement date
     */
    LocalDate calculateSettlementDate(LocalDate instructionDate, Currency currency);

    /**
     * Calculates daily trade value of outgoing or incoming instructions and sorts processed data by
     * settlement date.
     * @param instructions - instructions data
     * @param op - outgoing/incoming operation
     * @return - key/value pair of date anf settled amount
     */
    Map<LocalDate, BigDecimal> calculateDailySettledAmount(List<Instruction> instructions, Operation op);

    /**
     * Given a valid instruction, this method calculates trade value by the following formula:
     * Price per unit * Units * Agreed Fx rate
     * @param instruction
     * @return - amount corresponding to daily trade
     */
    BigDecimal calculateInstructionTradeAmount(Instruction instruction);

    /**
     * Calculates ranking of entities by highest outgoing/incoming instruction amount.Entity that instructs the
     * highest amount goes to the top.
     * @param instructions - instructions to be processed
     * @param op - outgoing/incoming operation
     * @return - key/value pairs of entity name and total instruction amount
     */
    Map<String,BigDecimal> rankEntitiesByInstructionAmount(List<Instruction> instructions, Operation op);

}
