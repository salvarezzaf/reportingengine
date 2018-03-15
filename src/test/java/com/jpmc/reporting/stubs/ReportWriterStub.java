package com.jpmc.reporting.stubs;

import com.jpmc.reporting.model.Operation;
import com.jpmc.reporting.output.ReportWriter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class ReportWriterStub implements ReportWriter {
    @Override
    public void writeReport(Map<?, BigDecimal> reportData, Currency currency, String reportType, Operation op) {
        System.out.println("writeReport method called");
    }

    @Override
    public void writeReportHeader() {

        System.out.println("writeReportHeader method called");

    }

    @Override
    public void writeReportFooter() {

        System.out.println("writeReportFooter method called");

    }
}
