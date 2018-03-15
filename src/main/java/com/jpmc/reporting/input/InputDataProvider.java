package com.jpmc.reporting.input;

import com.jpmc.reporting.model.Instruction;

import java.util.List;

public interface InputDataProvider {
    /**
     * Provides way to retrieve instructions data from desired input channel
     * @return instructions input data
     */
    List<Instruction> retrieveInstructions();
}
