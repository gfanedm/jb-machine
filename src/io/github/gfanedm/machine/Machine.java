package io.github.gfanedm.machine;

import java.util.List;

import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.memory.MemoryHandler;
import io.github.gfanedm.machine.pipeline.PipelineHandler;
import io.github.gfanedm.machine.program.ProgramFactory;

public class Machine {

	private static Machine machine;

	public MemoryHandler memoryHandler;
	public ProgramFactory programFactory;
	public PipelineHandler pipelineHandler;

	public static void main(String[] args) {
		new Machine();
	}

	public Machine() {
		machine = this;
		
		int opcode = 0, pc = 0;
		try {

			this.memoryHandler = new MemoryHandler(10);
			this.pipelineHandler = new PipelineHandler();

			this.programFactory = new ProgramFactory(pipelineHandler);

			List<Instruction> ins = programFactory.createRandomProgram(2);

			while (opcode != -1) {
				Instruction instruction = ins.get(pc);

				opcode = instruction.get(0);

				pipelineHandler.execute(memoryHandler, instruction);

				pc++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MemoryHandler getMemoryHandler() {
		return memoryHandler;
	}

	public PipelineHandler getPipelineHandler() {
		return pipelineHandler;
	}

	public ProgramFactory getProgramFactory() {
		return programFactory;
	}

	public static Machine getMachine() {
		return machine;
	}

}
