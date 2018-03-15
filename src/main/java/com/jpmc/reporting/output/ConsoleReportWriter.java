package com.jpmc.reporting.output;

import com.jpmc.reporting.model.Operation;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;

/**
 * Basic implementation of {@link ReportWriter}. This implementation
 * writes report data only to stdout. It requires a {@link PrintWriter} to
 * write data to stdout and also uses  {@link Locale} to provide internazionalized
 * version of final report where dates and amount get formatted according to the
 * country report is for.
 */
public class ConsoleReportWriter implements ReportWriter {

    private PrintWriter writer;
    private Locale locale;

    public ConsoleReportWriter(PrintWriter writer, Locale locale) {

        this.writer = writer;
        this.locale = locale;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeReport(Map<?, BigDecimal> reportData, Currency currency, String reportType, Operation op) {

        if (reportData == null || reportData.isEmpty() || reportType == null || currency == null
                || reportType.isEmpty() || op == null || op.toString().isEmpty())
            throw new IllegalArgumentException("Valid report data, report  type and operation are required for report output");

        String reportKey = "amountSettled".equals(reportType) ? "Date" : "EntityName";

        NumberFormat nf = getFormatterForCurrencyLocale();
        DateTimeFormatter dtf = getDateFormatterWithCurrentLocale();
        if ("Date".equals(reportKey))
            writer.printf("%nAmount in %s settled %s everyday:%n", currency.getCurrencyCode(), op.getOperation());
        else
            writer.printf("%nCurrent ranking of entities based on %s operations:%n", op.getOperation());

        reportData.forEach((k, v) -> {
            if (k instanceof LocalDate)
                writer.printf("%s: %s  Amount: %s%n", reportKey, ((LocalDate) k).format(dtf), nf.format(v));
            else
                writer.printf("%s: %s  Amount: %s%n", reportKey, k, nf.format(v));

        });

    }

    /**
     * {@inheritDoc}
     */
    public void writeReportHeader() {

        writer.printf("%n-------  JP Morgan Chase  -------%n");
        writer.printf("-------  Daily Forex Trading Report  -------%n");

    }

    /**
     * {@inheritDoc}
     */
    public void writeReportFooter() {

        writer.printf("%n-------  End Report  -------%n");
        writer.printf("-------  JP Morgan Chase Copyright \u00a9 2018  -------%n");

    }

    private NumberFormat getFormatterForCurrencyLocale() {

        return NumberFormat.getCurrencyInstance(this.locale);

    }

    private DateTimeFormatter getDateFormatterWithCurrentLocale() {

        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(this.locale);
    }

}
