package com.jpmc.reporting.output;

import com.jpmc.reporting.model.Operation;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface ReportWriter {

    void writeReport(Map<?, BigDecimal> reportData, Currency currency, String reportType, Operation op);

}
