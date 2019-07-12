package io.github.gfanedm.machine.methods;

import io.github.gfanedm.machine.instructions.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;

public class SumMethod extends Method {

	public SumMethod(int code) {
		super(code);
	}

	@Override
	public void executeFunction(MemoryHandler memoryHandler, Instruction instruction) {
		int pos1 = memoryHandler.getUseList().get(0).getWords().get(instruction.getList().get(0).getWord());
		int pos2 = memoryHandler.getUseList().get(1).getWords().get(instruction.getList().get(1).getWord());

		int sum = pos1 + pos2;

		memoryHandler.getUseList().get(2).getWords().set(instruction.getList().get(2).getWord(), sum);
	}

}
