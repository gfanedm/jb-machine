package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class HaltMethod extends Method {

	public HaltMethod(int code) {
		super(code);
	}

	@Override
	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {
		for (int j = 0; j < memoryHandler.getMemory().size(); j++) {
			System.out.print(String.format("%d » %s\n", j, memoryHandler.getMemory().get(j)));
		}
	}
}