package com.jpmc.reporting.output;

import com.jpmc.reporting.model.Operation;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

public class ConsoleReportWriter implements ReportWriter {

    @Override
    public void writeReport(Map<?, BigDecimal> reportData, Currency currency, String reportType, Operation op) {

        if (reportData == null || reportData.isEmpty() || reportType == null
                || reportType.isEmpty() || op == null || op.toString().isEmpty())
            throw new IllegalArgumentException("Valid report data, report  type and operation are required for report output");


        String reportKey = "amount".equals(reportType) ? "Date" : "EntityName";

        PrintWriter writer = new PrintWriter(System.out, true);

        NumberFormat currencyFormatter = getFormatterForCurrencyLocale(currency);

        writer.printf("Amount in %s settled %s everyday:%n", currency.getCurrencyCode(), op.toString());

        reportData.forEach((k, v) -> {
            if (k instanceof LocalDate)
                writer.printf("%s: %2$tD, Amount: %s", reportKey, k, currencyFormatter.format(v));
            else
                writer.printf("%s: %s, Amount: %s", reportKey, k, currencyFormatter.format(v));
        });

    }


    private NumberFormat getFormatterForCurrencyLocale(Currency currency) {

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        currencyFormatter.setCurrency(currency);
        return currencyFormatter;

    }

}
