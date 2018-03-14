package com.jpmc.reporting.engine;

import com.jpmc.reporting.input.InputDataProvider;
import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.output.ReportWriter;

import java.util.List;

public interface ReportingEngine {

   void generateReport(InputDataProvider inProvider, TradeOperationsProvider top, ReportWriter out);
}
