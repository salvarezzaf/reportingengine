package com.jpmc.reporting.engine;

import com.jpmc.reporting.input.InputDataProvider;
import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.output.ReportWriter;

import java.util.List;


public interface ReportingEngine {

    /**
     * Method in charge of generating the full forex report to the desired output channel.
     * It makes use of {@link InputDataProvider} to retrieve trade instructions and {@link TradeOperationsProvider}
     * to perform desired operations on instruction data. It then redirected process data the approprite
     * output channel by means of {@link ReportWriter}
     * @param inProvider - provider fo retrieval of input data
     * @param top - provider of operations applicable to forex trade instructions
     * @param out - desired channel for output data
     */
    void generateReport(InputDataProvider inProvider, TradeOperationsProvider top, ReportWriter out);
}
