package com.jpmc.reporting.model;

import com.jpmc.reporting.util.TestDataUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class InstructionValidationTest {

    private TestDataUtil dataUtil;
    private String errorMsg;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        dataUtil = new TestDataUtil();
        errorMsg = "Some mandatory fields on instruction were not set to correct values";

    }

    @Test
    public void instruction_WithEmptyEntity_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("",Operation.BUY,new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(),23,new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullEntity_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData(null,Operation.BUY,new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(),23,new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullOp_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",null,new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(),23,new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullAgreedFx_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",Operation.BUY,null, Currency.getInstance("EUR"), LocalDate.now(),23,new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullCurrency_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",Operation.BUY,new BigDecimal("1.13"), null, LocalDate.now(),23,new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullInstructionDate_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",Operation.BUY,new BigDecimal("1.13"), Currency.getInstance("EUR"),null,23,new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithZeroUnitValue_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",Operation.BUY,new BigDecimal("1.13"), Currency.getInstance("EUR"),LocalDate.now(),0,new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNegativeUnitValue_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",Operation.BUY,new BigDecimal("1.13"), Currency.getInstance("EUR"),LocalDate.now(),-1,new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullUnitPrice_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",Operation.BUY,new BigDecimal("1.13"), Currency.getInstance("EUR"),LocalDate.now(),23, null);

    }

    @Test
    public void instruction_WithNegativeUnitPrice_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",Operation.BUY,new BigDecimal("1.13"), Currency.getInstance("EUR"),LocalDate.now(),23, new BigDecimal("-1.20"));

    }

    @Test
    public void instruction_WithNegativeAgreedFx_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc",Operation.BUY,new BigDecimal("-1.13"), Currency.getInstance("EUR"),LocalDate.now(),23, new BigDecimal("120.4"));

    }

    @Test
    public void instruction_WithMixInvalidValue_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("",Operation.BUY,null, Currency.getInstance("EUR"),LocalDate.now(),0, null);

    }




}
