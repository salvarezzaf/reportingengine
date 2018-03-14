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

public class SimpleReportingEngine implements ReportingEngine {


    @Override
    public void generateReport(InputDataProvider inProvider, TradeOperationsProvider top, ReportWriter out) {

        List<Instruction> instructions = inProvider.retrieveInstructions();
        Map<LocalDate, BigDecimal> settledAmountOut = top.calculateDailySettledAmount(instructions, Operation.BUY);
        Map<LocalDate, BigDecimal> settledAmountIn = top.calculateDailySettledAmount(instructions, Operation.SELL);
        Map<String, BigDecimal> entityRankingOut = top.rankEntitiesByInstructionAmount(instructions, Operation.BUY);
        Map<String, BigDecimal> entityRankingIn = top.rankEntitiesByInstructionAmount(instructions, Operation.SELL);

        out.writeReportHeader();
        out.writeReport(settledAmountOut, Currency.getInstance("USD"), "amountSettled", Operation.BUY);
        out.writeReport(settledAmountIn, Currency.getInstance("USD"), "amountSettled", Operation.SELL);
        out.writeReport(entityRankingOut, Currency.getInstance("USD"), "ranking", Operation.BUY);
        out.writeReport(entityRankingIn, Currency.getInstance("USD"), "ranking", Operation.SELL);
        out.writeReportFooter();


    }
}
