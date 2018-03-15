package com.jpmc.reporting.output;

import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface ReportWriter {
    /**
     * Mathods in charge of sending the final forex trading report to the desired output channel
     * @param reportData data to write to output
     * @param currency  currency used for trading operations
     * @param reportType amountSettled or ranking report types
     * @param op outgoing/incoming trade operation
     */
    void writeReport(Map<?, BigDecimal> reportData, Currency currency, String reportType, Operation op);

    /**
     * Output the header part of the report
     */
    void writeReportHeader();

    /**
     * Output the footer part of the report
     */
    void writeReportFooter();

}
