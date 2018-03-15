package com.jpmc.reporting.engine;

import com.jpmc.reporting.input.InputDataProvider;
import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;
import com.jpmc.reporting.output.ReportWriter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;

/**
 *  Simple implementation of reporting engine. It orchestrates calls to
 *  components in charge of retrieving data, processing input and outputting data.
 *  In this implementation final report for forex trade is output directly to stdout
 */
public class SimpleReportingEngine implements ReportingEngine {

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateReport(InputDataProvider inProvider, TradeOperationsProvider top, ReportWriter out) {

        List<Instruction> instructions = inProvider.retrieveInstructions();
        Map<LocalDate, BigDecimal> settledAmountOut = top.calculateDailySettledAmount(instructions, Operation.BUY);
        Map<LocalDate, BigDecimal> settledAmountIn = top.calculateDailySettledAmount(instructions, Operation.SELL);
        Map<String, BigDecimal> entityRankingOut = top.rankEntitiesByInstructionAmount(instructions, Operation.BUY);
        Map<String, BigDecimal> entityRankingIn = top.rankEntitiesByInstructionAmount(instructions, Operation.SELL);

        out.writeReportHeader();
        out.writeReport(settledAmountIn, Currency.getInstance("USD"), "amountSettled", Operation.SELL);
        out.writeReport(settledAmountOut, Currency.getInstance("USD"), "amountSettled", Operation.BUY);
        out.writeReport(entityRankingIn, Currency.getInstance("USD"), "ranking", Operation.SELL);
        out.writeReport(entityRankingOut, Currency.getInstance("USD"), "ranking", Operation.BUY);
        out.writeReportFooter();


    }
}
