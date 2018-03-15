package com.jpmc.reporting.model;

import com.jpmc.reporting.util.TestDataUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

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

        dataUtil.addInstructionToTestData("", Operation.BUY, new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(), 23, new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullEntity_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData(null, Operation.BUY, new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(), 23, new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullOp_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", null, new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(), 23, new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullAgreedFx_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", Operation.BUY, null, Currency.getInstance("EUR"), LocalDate.now(), 23, new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullCurrency_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", Operation.BUY, new BigDecimal("1.13"), null, LocalDate.now(), 23, new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullInstructionDate_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", Operation.BUY, new BigDecimal("1.13"), Currency.getInstance("EUR"), null, 23, new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithZeroUnitValue_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", Operation.BUY, new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(), 0, new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNegativeUnitValue_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", Operation.BUY, new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(), -1, new BigDecimal("1.23"));

    }

    @Test
    public void instruction_WithNullUnitPrice_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", Operation.BUY, new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(), 23, null);

    }

    @Test
    public void instruction_WithNegativeUnitPrice_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", Operation.BUY, new BigDecimal("1.13"), Currency.getInstance("EUR"), LocalDate.now(), 23, new BigDecimal("-1.20"));

    }

    @Test
    public void instruction_WithNegativeAgreedFx_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("poc", Operation.BUY, new BigDecimal("-1.13"), Currency.getInstance("EUR"), LocalDate.now(), 23, new BigDecimal("120.4"));

    }

    @Test
    public void instruction_WithMixInvalidValue_ThrowsIllegalArgException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsg);

        dataUtil.addInstructionToTestData("", Operation.BUY, null, Currency.getInstance("EUR"), LocalDate.now(), 0, null);

    }

    @Test
    public void instruction_WithCallToStringMethod_ReturnsCorrectStringRepresentationOfObj() {
        List<Instruction> instructions = dataUtil.getInstructionsData();

        String expectedInstrctionStr = "Entity: foo, Operation: BUY, AgreedFx: 0.5, Currency: SGD, InstructionDate: 2016-01-01, " +
                "SettlementDate: 2016-01-05, Units: 200, unitPrice: 100.25";
        String actualInstructionStr = instructions.get(0).toString();

        assertThat(actualInstructionStr, equalTo(expectedInstrctionStr));
    }


}
