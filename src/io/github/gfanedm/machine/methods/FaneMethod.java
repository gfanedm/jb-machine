package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.Machine;
import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class FaneMethod extends Method {

	public FaneMethod(int code) {
		super(code);
	}

	@Override
	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {

		// The Fane method gets the multiplication of the n-1 numbers in the memory
		// and saves on the last position of the memory.

		// Setting the value of the last position to the value of the position one.
		memoryHandler.getMemory().replace(memoryHandler.getLines() - 1, memoryHandler.getMemory().get(0));

		// Making the successive sums.
		for (int i = 1; i < memoryHandler.getLines() - 1; i++) {
			Machine.getMachine().getPipelineHandler().getMethod(1).executeFunction(memoryHandler,
					new Instruction(0, i, memoryHandler.getLines() - 1, memoryHandler.getLines() - 1));
		}

	}

}
