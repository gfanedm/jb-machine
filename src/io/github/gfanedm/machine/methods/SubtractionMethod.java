package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class SubtractionMethod extends Method {

	public SubtractionMethod(int code) {
		super(code);
	}

	@Override
	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {
		memoryHandler.getMemory().replace(instruction.get(3),
				memoryHandler.getMemory().get(instruction.get(1)) - memoryHandler.getMemory().get(instruction.get(2)));
	}

}
