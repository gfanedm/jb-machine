package io.github.gfanedm.machine.program;

import java.util.List;
import java.util.Random;

import io.github.gfanedm.machine.instruction.Instruction;
import io.github.gfanedm.machine.map.SizeableList;
import io.github.gfanedm.machine.pipeline.PipelineHandler;

public class ProgramFactory {

	private final PipelineHandler pipelineHandler;

	public ProgramFactory(PipelineHandler pipelineHandler) {
		this.pipelineHandler = pipelineHandler;
	}

	private static final Random RANDOM = new Random();

	public List<Instruction> createRandomProgram(int size) throws Exception {
		if (size <= 0) {
			throw new Exception("The program can't create the random program with size <= 0");
		}

		try {
			List<Instruction> list = new SizeableList<Instruction>(size);

			for (int i = 0; i < (size - 1); i++) {

				//list.add(new Instruction(getRandom().nextInt(pipelineHandler.getMethods().size() - 1) + 1, i, i + 1, i + 2));
				list.add(new Instruction(7, i, i + 1, i + 2));
			}

			list.add(new Instruction(-1));

			return list;

		} catch (Exception exception) {
			throw exception;
		}
	}

	public Random getRandom() {
		return RANDOM;
	}

	public PipelineHandler getPipelineHandler() {
		return pipelineHandler;
	}
}
