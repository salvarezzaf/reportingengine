package com.jpmc.reporting.engine;

import com.jpmc.reporting.input.InputDataProvider;
import com.jpmc.reporting.input.SimpleInputDataProvider;
import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SimpleTradeOperationsProviderTest {

    private TradeOperationsProvider tradeOpsProvider;
    private InputDataProvider dataProvider;
    private LocalDate aMondayDate;
    private LocalDate aSaturdayDate;
    private Currency aed;
    private Currency sar;
    private Currency euro;
    private List<Instruction> instructions;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {

        tradeOpsProvider = new SimpleTradeOperationsProvider();
        dataProvider = new SimpleInputDataProvider();
        aMondayDate = LocalDate.of(2016, Month.FEBRUARY, 8);
        aSaturdayDate = LocalDate.of(2016, Month.MARCH, 12);
        aed = Currency.getInstance("AED");
        sar = Currency.getInstance("SAR");
        euro = Currency.getInstance("EUR");
        instructions = dataProvider.retrieveInstructions();
    }

    @Test
    public void calculateSettlementDate_WithSaturdayDateAndEUR_ReturnsMondaysDate() {
        LocalDate settlementDate = tradeOpsProvider.calculateSettlementDate(aSaturdayDate, euro);
        assertThat(settlementDate.getDayOfWeek(), equalTo(DayOfWeek.MONDAY));
        assertThat(settlementDate.getYear(), equalTo(2016));
        assertThat(settlementDate.getMonth(), equalTo(Month.MARCH));
        assertThat(settlementDate.getDayOfMonth(), equalTo(14));
    }

    @Test
    public void calculateSettlementDate_WithMondayDateAndEUR_ReturnsTuesdaysDate() {
        LocalDate settlementDate = tradeOpsProvider.calculateSettlementDate(aMondayDate, euro);
        assertThat(settlementDate.getDayOfWeek(), equalTo(DayOfWeek.TUESDAY));
        assertThat(settlementDate.getYear(), equalTo(2016));
        assertThat(settlementDate.getMonth(), equalTo(Month.FEBRUARY));
        assertThat(settlementDate.getDayOfMonth(), equalTo(9));
    }

    @Test
    public void calculateSettlementDate_WithSaturdayDateAndAED_ReturnsSundayssDate() {
        LocalDate settlementDate = tradeOpsProvider.calculateSettlementDate(aSaturdayDate, aed);
        assertThat(settlementDate.getDayOfWeek(), equalTo(DayOfWeek.SUNDAY));
        assertThat(settlementDate.getYear(), equalTo(2016));
        assertThat(settlementDate.getMonth(), equalTo(Month.MARCH));
        assertThat(settlementDate.getDayOfMonth(), equalTo(13));
    }

    @Test
    public void calculateSettlementDate_WithMondayDateAndAED_ReturnsThursdaysDate() {
        LocalDate settlementDate = tradeOpsProvider.calculateSettlementDate(aMondayDate, aed);
        assertThat(settlementDate.getDayOfWeek(), equalTo(DayOfWeek.THURSDAY));
        assertThat(settlementDate.getYear(), equalTo(2016));
        assertThat(settlementDate.getMonth(), equalTo(Month.FEBRUARY));
        assertThat(settlementDate.getDayOfMonth(), equalTo(11));
    }

    @Test
    public void calculateSettlementDate_WithSaturdayDateAndSAR_ReturnsSundayssDate() {
        LocalDate settlementDate = tradeOpsProvider.calculateSettlementDate(aSaturdayDate, sar);
        assertThat(settlementDate.getDayOfWeek(), equalTo(DayOfWeek.SUNDAY));
        assertThat(settlementDate.getYear(), equalTo(2016));
        assertThat(settlementDate.getMonth(), equalTo(Month.MARCH));
        assertThat(settlementDate.getDayOfMonth(), equalTo(13));
    }

    @Test
    public void calculateSettlementDate_WithMondayDateAndSAR_ReturnsThursdaysDate() {
        LocalDate settlementDate = tradeOpsProvider.calculateSettlementDate(aMondayDate, sar);
        assertThat(settlementDate.getDayOfWeek(), equalTo(DayOfWeek.THURSDAY));
        assertThat(settlementDate.getYear(), equalTo(2016));
        assertThat(settlementDate.getMonth(), equalTo(Month.FEBRUARY));
        assertThat(settlementDate.getDayOfMonth(), equalTo(11));
    }

    @Test
    public void calculateSettlementDate_WithInvalidArguments_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("A valid date and currency symbol are required for settlement date calculation");
        tradeOpsProvider.calculateSettlementDate(null,sar);
    }

    @Test
    public void calculateInstructionTradeAmount_WithFirstInstruction_ReturnsCorrectAmountPerFormula() {
        BigDecimal expectedTradeAmount = new BigDecimal("10025").setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal actualTradeAmount = tradeOpsProvider.calculateInstructionTradeAmount(instructions.get(0));
        assertEquals(actualTradeAmount, expectedTradeAmount);

    }

    @Test
    public void calculateInstructionTradeAmount_WithSecondInstruction_ReturnsCorrectAmountPerFormula() {
        BigDecimal expectedTradeAmount = new BigDecimal("14899.50").setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal actualTradeAmount = tradeOpsProvider.calculateInstructionTradeAmount(instructions.get(1));
        assertEquals(actualTradeAmount, expectedTradeAmount);

    }

    @Test
    public void calculateInstructionTradeAmount_WithInvalidInstruction_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Instruction must not be null for trade amount calculation");
        tradeOpsProvider.calculateInstructionTradeAmount(null);
    }

    @Test
    public void calculateDailySettledAmount_WithInstructionsAndBuyFlag_ReturnsSettledAmountSortedByDate() {
        Map<LocalDate,BigDecimal> expectedBuySettledAmount = new LinkedHashMap<>();
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.JANUARY,4),new BigDecimal("10025.00"));
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.JUNE,9),new BigDecimal("22782.38"));
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.JULY,7),new BigDecimal("66881.23"));

        Map<LocalDate,BigDecimal> settledAmountForBuyOp = tradeOpsProvider.calculateDailySettledAmount(instructions,Operation.BUY);
        assertThat(settledAmountForBuyOp.entrySet(), equalTo(expectedBuySettledAmount.entrySet()));
    }

    @Test
    public void calculateDailySettledAmount_WithInstructionsAndSellFlag_ReturnsSettledAmountSortedByDate() {
        Map<LocalDate,BigDecimal> expectedBuySettledAmount = new LinkedHashMap<>();
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.APRIL,9),new BigDecimal("14899.50"));
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.SEPTEMBER,9),new BigDecimal("10408.20"));
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.OCTOBER,17),new BigDecimal("19822.32"));

        Map<LocalDate,BigDecimal> settledAmountForBuyOp = tradeOpsProvider.calculateDailySettledAmount(instructions,Operation.SELL);
        assertThat(settledAmountForBuyOp.entrySet(), equalTo(expectedBuySettledAmount.entrySet()));
    }

    @Test
    public void calculateDailySettledAmount_WithEmptyInstructionList_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Instructions and operations must not be null/empty for amount settled calculation");
        tradeOpsProvider.calculateDailySettledAmount(new ArrayList<>(), Operation.BUY);
    }

    @Test
    public void calculateDailySettledAmount_WithNullInstructionList_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Instructions and operations must not be null/empty for amount settled calculation");
        tradeOpsProvider.calculateDailySettledAmount(null, Operation.BUY);
    }

    @Test
    public void calculateDailySettledAmount_WithNullOperation_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Instructions and operations must not be null/empty for amount settled calculation");
        tradeOpsProvider.calculateDailySettledAmount(instructions, null);
    }

    @Test
    public void rankEntitiesByInstructionAmount_WithInstructionsAndBuyFlag_ReturnsSettledAmountSortedByDate() {
        Map<LocalDate,BigDecimal> expectedBuySettledAmount = new LinkedHashMap<>();
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.APRIL,9),new BigDecimal("14899.50"));
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.SEPTEMBER,9),new BigDecimal("10408.20"));
        expectedBuySettledAmount.put(LocalDate.of(2016,Month.OCTOBER,17),new BigDecimal("19822.32"));

        Map<String,BigDecimal> entitiesRanking = tradeOpsProvider.rankEntitiesByInstructionAmount(instructions,Operation.BUY);
        System.out.print(entitiesRanking);

    }
}
