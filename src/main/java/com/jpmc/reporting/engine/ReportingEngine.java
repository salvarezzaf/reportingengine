package com.jpmc.reporting.engine;

import com.jpmc.reporting.model.Instruction;

import java.util.List;

public interface ReportingEngine {

   void generateReport(List<Instruction> instructions);

}
