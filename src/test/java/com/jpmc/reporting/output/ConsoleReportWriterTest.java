package com.jpmc.reporting.output;

import com.jpmc.reporting.model.Operation;
import com.jpmc.reporting.util.TestDataUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ConsoleReportWriterTest {

    private ByteArrayOutputStream outContent;
    private ReportWriter reportWriter;
    private TestDataUtil testDataUtil;
    private String exceptionMsg;

    @Rule
    public ExpectedException thown = ExpectedException.none();

    @Before
    public void setup() {
        outContent = new ByteArrayOutputStream();
        reportWriter = new ConsoleReportWriter(new PrintWriter(outContent,true), Locale.US);
        testDataUtil = new TestDataUtil();
        exceptionMsg = "Valid report data, report  type and operation are required for report output";
    }

    @Test
    public void writeReportHeader_WithNoArguments_ReturnsDailyReportHeader() {
        reportWriter.writeReportHeader();
        String expectedOutput = "\n-------  JP Morgan Chase  -------\n" +
                                "-------  Daily Forex Trading Report  -------\n";
        assertThat(outContent.toString(), equalTo(expectedOutput));

    }


    @Test
    public void writeReportFooter_WithNoArguments_ReturnsDailyReportFooter() {

        reportWriter.writeReportFooter();
        String expectedOutput = "-------  End Report  -------\n" +
                "-------  JP Morgan Chase Copyright \u00a9 2018  -------\n";
        assertThat(outContent.toString(), equalTo(expectedOutput));

    }

    @Test
    public void writeReport_WithSettledDataForBuyOp_ReturnsReportWithDataProperlyFormatted() {
        Map<LocalDate,BigDecimal> expectedSettledForBuy = testDataUtil.getExpectedSettleAmountForOp(Operation.BUY);
        reportWriter.writeReport(expectedSettledForBuy, Currency.getInstance("USD"),"amountSettled",Operation.BUY);
        assertThat(outContent.toString(), equalTo(testDataUtil.getExpectedReportForSettledAmountAndOp(Operation.BUY)));

    }

    @Test
    public void writeReport_WithSettledDataForSellOp_ReturnsReportWithDataProperlyFormatted() {
        Map<LocalDate,BigDecimal> expectedSettledForBuy = testDataUtil.getExpectedSettleAmountForOp(Operation.SELL);
        reportWriter.writeReport(expectedSettledForBuy, Currency.getInstance("USD"),"amountSettled",Operation.SELL);
        assertThat(outContent.toString(), equalTo(testDataUtil.getExpectedReportForSettledAmountAndOp(Operation.SELL)));

    }

    @Test
    public void writeReport_WithRankingForBuyOp_ReturnsReportWithDataProperlyFormatted() {
        Map<String,BigDecimal> expectedRankingForBuy = testDataUtil.getExpectedEntityRankingForOp(Operation.BUY,false);
        reportWriter.writeReport(expectedRankingForBuy, Currency.getInstance("USD"),"ranking",Operation.BUY);
        assertThat(outContent.toString(), equalTo(testDataUtil.getExpectedReportForRankingByOp(Operation.BUY)));

    }

    @Test
    public void writeReport_WithRankingForSellOp_ReturnsReportWithDataProperlyFormatted() {
        Map<String,BigDecimal> expectedRankingForSell = testDataUtil.getExpectedEntityRankingForOp(Operation.SELL,false);
        reportWriter.writeReport(expectedRankingForSell, Currency.getInstance("USD"),"ranking",Operation.SELL);
        assertThat(outContent.toString(), equalTo(testDataUtil.getExpectedReportForRankingByOp(Operation.SELL)));
    }


    @Test
    public void writeReport_WithEmptyRankingData_ThrowsIllegalArgException(){
        thown.expect(IllegalArgumentException.class);
        thown.expectMessage(exceptionMsg);

        reportWriter.writeReport(new LinkedHashMap<>(),Currency.getInstance("USD"),"ranking",Operation.BUY);
    }

    @Test
    public void writeReport_WithNullRankingData_ThrowsIllegalArgException(){
        thown.expect(IllegalArgumentException.class);
        thown.expectMessage(exceptionMsg);

        reportWriter.writeReport(null,Currency.getInstance("USD"),"ranking",Operation.BUY);
    }

    @Test
    public void writeReport_WithEmptyReportType_ThrowsIllegalArgException(){
        thown.expect(IllegalArgumentException.class);
        thown.expectMessage(exceptionMsg);
        Map<String,BigDecimal> expectedRankingForSell = testDataUtil.getExpectedEntityRankingForOp(Operation.SELL,false);
        reportWriter.writeReport(expectedRankingForSell,Currency.getInstance("USD"),"",Operation.BUY);
    }

    @Test
    public void writeReport_WithNullCurrency_ThrowsIllegalArgException(){
        thown.expect(IllegalArgumentException.class);
        thown.expectMessage(exceptionMsg);
        Map<String,BigDecimal> expectedRankingForSell = testDataUtil.getExpectedEntityRankingForOp(Operation.SELL,false);
        reportWriter.writeReport(expectedRankingForSell,null,"ranking",Operation.BUY);
    }

    @Test
    public void writeReport_WithNullOperation_ThrowsIllegalArgException(){
        thown.expect(IllegalArgumentException.class);
        thown.expectMessage(exceptionMsg);
        Map<String,BigDecimal> expectedRankingForSell = testDataUtil.getExpectedEntityRankingForOp(Operation.SELL,false);
        reportWriter.writeReport(expectedRankingForSell,Currency.getInstance("USD"),"ranking",null);
    }



}
