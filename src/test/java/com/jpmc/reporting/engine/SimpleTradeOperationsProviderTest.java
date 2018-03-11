package com.jpmc.reporting.engine;

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
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SimpleTradeOperationsProviderTest {

    private TradeOperationsProvider tradeOpsProvider;
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
        aMondayDate = LocalDate.of(2016, Month.FEBRUARY, 8);
        aSaturdayDate = LocalDate.of(2016, Month.MARCH, 12);
        aed = Currency.getInstance("AED");
        sar = Currency.getInstance("SAR");
        euro = Currency.getInstance("EUR");
        instructions = new ArrayList<>();

        Instruction firstInstruction =
                new Instruction.Builder()
                        .entity("foo")
                        .tradeOperation(Operation.BUY)
                        .agreedFx(new BigDecimal(0.50))
                        .currency(Currency.getInstance("EUR"))
                        .instructionDate(LocalDate.of(2016, Month.JANUARY, 1))
                        .settlementDate(LocalDate.of(2016, Month.JANUARY, 2))
                        .units(200)
                        .unitPrice(new BigDecimal(100.25))
                        .build();

        Instruction secondInstruction =
                new Instruction.Builder()
                        .entity("bar")
                        .tradeOperation(Operation.SELL)
                        .agreedFx(new BigDecimal(0.22))
                        .currency(Currency.getInstance("AED"))
                        .instructionDate(LocalDate.of(2016, Month.JANUARY, 5))
                        .settlementDate(LocalDate.of(2016, Month.FEBRUARY, 5))
                        .units(450)
                        .unitPrice(new BigDecimal(150.5))
                        .build();

//        Instruction thirdInstruction =
//                new Instruction.Builder()
//                        .entity("bar")
//                        .tradeOperation(Operation.SELL)
//                        .agreedFx(new BigDecimal(0.22))
//                        .currency(Currency.getInstance("AED"))
//                        .instructionDate(LocalDate.of(2016, Month.MAY, 5))
//                        .settlementDate(LocalDate.of(2016, Month.MAY, 17))
//                        .units(380)
//                        .unitPrice(new BigDecimal(150.5))
//                        .build();
//
//        Instruction forthInstruction =
//                new Instruction.Builder()
//                        .entity("qux")
//                        .tradeOperation(Operation.SELL)
//                        .agreedFx(new BigDecimal(0.27))
//                        .currency(Currency.getInstance("SAR"))
//                        .instructionDate(LocalDate.of(2016, Month.JUNE, 5))
//                        .settlementDate(LocalDate.of(2016, Month.JUNE, 17))
//                        .units(451)
//                        .unitPrice(new BigDecimal(250.5))
//                        .build();
//
//        Instruction fifthInstruction =
//
//                new Instruction.Builder()
//                        .entity("quux")
//                        .tradeOperation(Operation.BUY)
//                        .agreedFx(new BigDecimal(3.26))
//                        .currency(Currency.getInstance("AED"))
//                        .instructionDate(LocalDate.of(2016, Month.JUNE, 18))
//                        .settlementDate(LocalDate.of(2016, Month.JUNE, 19))
//                        .units(551)
//                        .unitPrice(new BigDecimal(160.5))
//                        .build();

        instructions.add(firstInstruction);
        instructions.add(secondInstruction);

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
    public void calculateInstructionTradeAmount_WithValidInstruction_ReturnsCorrectAmountPerFormula() {
        BigDecimal expectedTradeAmount = new BigDecimal("10025").setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal actualTradeAmount = tradeOpsProvider.calculateInstructionTradeAmount(instructions.get(0));
        assertEquals(actualTradeAmount, expectedTradeAmount);

    }

    @Test
    public void calculateInstructionTradeAmount_WithInvalidInstruction_ThrowsIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        tradeOpsProvider.calculateInstructionTradeAmount(null);
    }

}
