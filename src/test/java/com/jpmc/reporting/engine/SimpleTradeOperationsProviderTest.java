package com.jpmc.reporting.engine;

import com.jpmc.reporting.model.Instruction;
import com.jpmc.reporting.model.Operation;
import com.jpmc.reporting.util.TestDataUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;


public class SimpleTradeOperationsProviderTest {

    private TradeOperationsProvider tradeOpsProvider;
    private TestDataUtil testDataUtil;
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
        testDataUtil = new TestDataUtil();
        aMondayDate = LocalDate.of(2016, Month.FEBRUARY, 8);
        aSaturdayDate = LocalDate.of(2016, Month.MARCH, 12);
        aed = Currency.getInstance("AED");
        sar = Currency.getInstance("SAR");
        euro = Currency.getInstance("EUR");
        instructions = testDataUtil.getInstructionsData();
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
        tradeOpsProvider.calculateSettlementDate(null, sar);
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
        Map<LocalDate, BigDecimal> expectedBuySettledAmount = testDataUtil.getExpectedSettleAmountForOp(Operation.BUY);

        Map<LocalDate, BigDecimal> actualBuySettledAmount = tradeOpsProvider.calculateDailySettledAmount(instructions, Operation.BUY);
        assertThat(actualBuySettledAmount.entrySet(), equalTo(expectedBuySettledAmount.entrySet()));
    }

    @Test
    public void calculateDailySettledAmount_WithInstructionsAndSellFlag_ReturnsSettledAmountSortedByDate() {
        Map<LocalDate, BigDecimal> expectedBuySettledAmount = testDataUtil.getExpectedSettleAmountForOp(Operation.SELL);

        Map<LocalDate, BigDecimal> settledAmountForBuyOp = tradeOpsProvider.calculateDailySettledAmount(instructions, Operation.SELL);

        assertThat(settledAmountForBuyOp.entrySet(), equalTo(expectedBuySettledAmount.entrySet()));
    }

    @Test
    public void calculateDailySettledAmount_WithTwoInstructionsOnSameDateAndBuyFlag_ReturnsAggregatedAmountForThatDate() {
        testDataUtil.addInstructionToTestData("bmw", Operation.BUY, new BigDecimal("0.75"), Currency.getInstance("SGD"), LocalDate.of(2016, Month.JULY, 6), 342, new BigDecimal("165.7"));

        Map<LocalDate, BigDecimal> actualAggregatedSettledAmount = tradeOpsProvider.calculateDailySettledAmount(instructions, Operation.BUY);
        Map<LocalDate, BigDecimal> expectedAggregatedSettledAmount = testDataUtil.getExpectedSettleAmountForOp(Operation.BUY);
        expectedAggregatedSettledAmount.put(LocalDate.of(2016, Month.JULY, 07), new BigDecimal("109383.28"));

        assertThat(actualAggregatedSettledAmount.entrySet(), equalTo(expectedAggregatedSettledAmount.entrySet()));
    }

    @Test
    public void calculateDailySettledAmount_WithTwoInstructionsOnSameDateAndSellFlag_ReturnsAggregatedAmountForThatDate() {
        testDataUtil.addInstructionToTestData("bmw", Operation.SELL, new BigDecimal("1.45"), Currency.getInstance("SGD"), LocalDate.of(2016, Month.OCTOBER, 15), 532, new BigDecimal("135.7"));

        Map<LocalDate, BigDecimal> actualAggregatedSettledAmount = tradeOpsProvider.calculateDailySettledAmount(instructions, Operation.SELL);
        Map<LocalDate, BigDecimal> expectedAggregatedSettledAmount = testDataUtil.getExpectedSettleAmountForOp(Operation.SELL);
        expectedAggregatedSettledAmount.put(LocalDate.of(2016, Month.OCTOBER, 17), new BigDecimal("124501.30"));

        assertThat(actualAggregatedSettledAmount.entrySet(), equalTo(expectedAggregatedSettledAmount.entrySet()));
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
    public void rankEntitiesByInstructionAmount_WithInstructionsAndBuyFlag_ReturnsXyzAsHighestInstruction() {
        Map<String, BigDecimal> expectedBuyEntityRanking = testDataUtil.getExpectedEntityRankingForOp(Operation.BUY, false);

        Map<String, BigDecimal> actualBuyEntityRanking = tradeOpsProvider.rankEntitiesByInstructionAmount(instructions, Operation.BUY);

        assertThat(actualBuyEntityRanking.entrySet(), equalTo(expectedBuyEntityRanking.entrySet()));

    }

    @Test
    public void rankEntitiesByInstructionAmount_WithInstructionsAndSellFlag_ReturnsMacAsHighestInstruction() {
        Map<String, BigDecimal> expectedBuyEntityRanking = testDataUtil.getExpectedEntityRankingForOp(Operation.SELL, false);

        Map<String, BigDecimal> actualBuyEntityRanking = tradeOpsProvider.rankEntitiesByInstructionAmount(instructions, Operation.SELL);

        assertThat(actualBuyEntityRanking.entrySet(), equalTo(expectedBuyEntityRanking.entrySet()));

    }

    @Test
    public void rankEntitiesByInstructionAmount_WithTwoInstructionsWithSameRankAndBuyFlag_ReturnsXyZAndBmwAsHighestInRank() {

        testDataUtil.addInstructionToTestData("bmw", Operation.BUY, new BigDecimal("1.06"), Currency.getInstance("GBP"), LocalDate.of(2016, Month.JULY, 6), 571, new BigDecimal("110.5"));

        Map<String, BigDecimal> actualTieEntityRanking = tradeOpsProvider.rankEntitiesByInstructionAmount(instructions, Operation.BUY);
        Map<String, BigDecimal> expectedTieEntityRanking = testDataUtil.getExpectedEntityRankingForOp(Operation.BUY, true);

        assertThat(actualTieEntityRanking.entrySet(), equalTo(expectedTieEntityRanking.entrySet()));


    }

    @Test
    public void rankEntitiesByInstructionAmount_WithTwoInstructionsWithSameRankAndSellFlag_ReturnsMacAndPocAsHighestInRank() {

        testDataUtil.addInstructionToTestData("poc", Operation.SELL, new BigDecimal("1.12"), Currency.getInstance("EUR"), LocalDate.of(2016, Month.JULY, 6), 171, new BigDecimal("103.5"));

        Map<String, BigDecimal> actualTieEntityRanking = tradeOpsProvider.rankEntitiesByInstructionAmount(instructions, Operation.SELL);
        Map<String, BigDecimal> expectedTieEntityRanking = testDataUtil.getExpectedEntityRankingForOp(Operation.SELL, true);

        assertThat(actualTieEntityRanking.entrySet(), equalTo(expectedTieEntityRanking.entrySet()));

    }

    @Test
    public void rankEntitiesByInstructionAmount_WithEmptyInstructionList_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Instructions and operations must not be null/empty for entity ranking");
        tradeOpsProvider.rankEntitiesByInstructionAmount(new ArrayList<>(), Operation.BUY);
    }

    @Test
    public void rankEntitiesByInstructionAmount_WithNullInstructionList_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Instructions and operations must not be null/empty for entity ranking");
        tradeOpsProvider.rankEntitiesByInstructionAmount(null, Operation.BUY);
    }

    @Test
    public void rankEntitiesByInstructionAmount_WithNullOperation_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Instructions and operations must not be null/empty for entity ranking");
        tradeOpsProvider.rankEntitiesByInstructionAmount(instructions, null);
    }

}
