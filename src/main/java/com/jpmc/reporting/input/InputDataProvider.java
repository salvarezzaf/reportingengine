package com.jpmc.reporting.input;

import com.jpmc.reporting.model.Instruction;

import java.util.List;

public interface InputDataProvider {
    List<Instruction> retrieveInstructions();
}
