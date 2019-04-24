package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.Machine;
import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class PowMethod extends Method {

	public PowMethod(int code) {
		super(code);
	}

	@Override
	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {
		Machine.getMachine().getPipelineHandler().getMethod(3).executeFunction(memoryHandler, new Instruction(0, instruction.get(1), instruction.get(1), instruction.get(3)));
		for (int i = 0; i < memoryHandler.getMemory().get(instruction.get(2)) - 2; i++) {
			Machine.getMachine().getPipelineHandler().getMethod(3).executeFunction(memoryHandler, new Instruction(0, instruction.get(1), instruction.get(3), instruction.get(3)));
		}
	}

}
