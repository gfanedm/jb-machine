package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.Machine;
import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class MultiplyMethod extends Method {

	public MultiplyMethod(int code) {
		super(code);
	}

	@Override
	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {
		int pos1 = instruction.get(1);
		int pos3 = instruction.get(3);

		int valuePos2 = memoryHandler.getMemory().get(instruction.get(2));
 
		// Setting the value of the position three to the value of the position one.
		memoryHandler.getMemory().replace(pos3, memoryHandler.getMemory().get(pos1));

		// Making the successive sums.
		for (int i = 0; i < valuePos2 - 1; i++) {
			Machine.getMachine().getPipelineHandler().getMethod(1).executeFunction(memoryHandler, new Instruction(0, pos1, pos3, pos3));
		}

		// System.out.println("Multiply = " + memoryHandler.getMemory().get(pos1) + " * " + valuePos2 + " = "
		// + (memoryHandler.getMemory().get(pos1) * valuePos2));
		// System.out.println("MTP = " + memoryHandler.getMemory().get(pos3));
	}

}
