package com.jpmc.reporting;

import com.jpmc.reporting.engine.ReportingEngine;
import com.jpmc.reporting.engine.SimpleReportingEngine;
import com.jpmc.reporting.engine.SimpleTradeOperationsProvider;
import com.jpmc.reporting.engine.TradeOperationsProvider;
import com.jpmc.reporting.input.InputDataProvider;
import com.jpmc.reporting.input.SimpleInputDataProvider;
import com.jpmc.reporting.output.ConsoleReportWriter;
import com.jpmc.reporting.output.ReportWriter;

import java.io.PrintWriter;
import java.util.Locale;

/**
 * Daily Trade Reporting Engine
 * @author Fabio Salvarezza
 * @version 1.0
 */
public class Application {

    public static void main(String[] args) {

        try {

            InputDataProvider dataIn = new SimpleInputDataProvider();
            TradeOperationsProvider top = new SimpleTradeOperationsProvider();
            ReportWriter dataOut = new ConsoleReportWriter(new PrintWriter(System.out, true), Locale.US);
            ReportingEngine engine = new SimpleReportingEngine();
            engine.generateReport(dataIn, top, dataOut);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
