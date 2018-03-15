package com.jpmc.reporting.engine;

import com.jpmc.reporting.input.InputDataProvider;
import com.jpmc.reporting.output.ReportWriter;
import com.jpmc.reporting.stubs.InputDataProviderStub;
import com.jpmc.reporting.stubs.ReportWriterStub;
import com.jpmc.reporting.stubs.TradeOperationsProviderStub;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class SimpleReportingEngineTest {

    private InputDataProvider stubInProvider;
    private TradeOperationsProvider topStub;
    private ReportWriter writerStub;
    private ReportingEngine engine;
    private ByteArrayOutputStream outContent;

    @Before
    public void setup() {

        engine = new SimpleReportingEngine();
        stubInProvider = new InputDataProviderStub();
        topStub = new TradeOperationsProviderStub();
        writerStub = new ReportWriterStub();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void generateReport_WithStubsForProviders_ReturnsThatEachStubMethodIsCalled() {
           String expectedOutput = "retrieveInstructions method called. Returning empty list\n" +
                   "calculateDailySettledAmount method called\n" +
                   "calculateDailySettledAmount method called\n" +
                   "rankEntitiesByInstructionAmount method called\n" +
                   "rankEntitiesByInstructionAmount method called\n" +
                   "writeReportHeader method called\n" +
                   "writeReport method called\n" +
                   "writeReport method called\n" +
                   "writeReport method called\n" +
                   "writeReport method called\n" +
                   "writeReportFooter method called\n";

           engine.generateReport(stubInProvider,topStub,writerStub);
           assertThat(outContent.toString(), equalTo(expectedOutput));

    }
}
