package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.instructions.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class HaltMethod extends Method {

	public HaltMethod(int code) {
		super(code);
	}

	@Override
	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {
		// System.out.println("Halt");
	}
}