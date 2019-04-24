package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.Machine;
import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class DistanceMethod extends Method {

	public DistanceMethod(int code) {
		super(code);
	}

	@Override
	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {
		Machine.getMachine().getPipelineHandler().getMethod(3).executeFunction(memoryHandler, new Instruction(0, instruction.get(1), instruction.get(2), instruction.get(3)));
	}

}
