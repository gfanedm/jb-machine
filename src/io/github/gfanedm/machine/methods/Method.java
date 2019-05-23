package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.instructions.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class Method {

	private int opCode;

	public Method(int opCode) {
		this.opCode = opCode;
	}

	public int getOpCode() {
		return opCode;
	}

	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {

	}

}
