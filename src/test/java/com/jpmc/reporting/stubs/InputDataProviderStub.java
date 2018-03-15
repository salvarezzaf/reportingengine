package com.jpmc.reporting.stubs;

import com.jpmc.reporting.input.InputDataProvider;
import com.jpmc.reporting.model.Instruction;

import java.util.ArrayList;
import java.util.List;

public class InputDataProviderStub implements InputDataProvider{
    @Override
    public List<Instruction> retrieveInstructions() {
        System.out.println("retrieveInstructions method called. Returning empty list");
        return new ArrayList<>();
    }
}
