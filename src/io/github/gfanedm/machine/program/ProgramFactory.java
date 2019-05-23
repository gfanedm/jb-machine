package io.github.gfanedm.machine.program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

import io.github.gfanedm.machine.Machine;
import io.github.gfanedm.machine.instructions.Instruction;
import io.github.gfanedm.machine.map.SizeableList;
import io.github.gfanedm.machine.memory.MemoryChecker.MemoryAddress;
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
				// list.add(new Instruction(getRandom().nextInt(3) + 1, i, i + 1, i + 2));\
				int opCode = getRandom().nextInt(3) + 1;

				MemoryAddress a1 = new MemoryAddress(getRandom().nextInt(Machine.RAM_SIZE),
						getRandom().nextInt(Machine.WORDS_SIZE));
				MemoryAddress a2 = new MemoryAddress(getRandom().nextInt(Machine.RAM_SIZE),
						getRandom().nextInt(Machine.WORDS_SIZE));
				MemoryAddress a3 = new MemoryAddress(getRandom().nextInt(Machine.RAM_SIZE),
						getRandom().nextInt(Machine.WORDS_SIZE));

				list.add(new Instruction(opCode, a1, a2, a3));
			}

			list.add(new Instruction(-1,
					new MemoryAddress(getRandom().nextInt(Machine.RAM_SIZE), getRandom().nextInt(Machine.WORDS_SIZE)),
					new MemoryAddress(getRandom().nextInt(Machine.RAM_SIZE), getRandom().nextInt(Machine.WORDS_SIZE)),
					new MemoryAddress(getRandom().nextInt(Machine.RAM_SIZE), getRandom().nextInt(Machine.WORDS_SIZE))));

			return list;

		} catch (Exception exception) {
			throw exception;
		}
	}

	public List<Instruction> fileProgram(String name) throws Exception {

		try {

			File file = new File(name);

			if (!file.exists())
				throw new Exception("File");

			List<Instruction> list = new SizeableList<Instruction>();

			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line = null;

			while ((line = reader.readLine()) != null) {
				String[] str = line.split(" ");
				int opCode = Integer.valueOf(str[0]);

				MemoryAddress a1 = new MemoryAddress(Integer.valueOf(str[1]), Integer.valueOf(str[2]));
				MemoryAddress a2 = new MemoryAddress(Integer.valueOf(str[3]), Integer.valueOf(str[4]));
				MemoryAddress a3 = new MemoryAddress(Integer.valueOf(str[5]), Integer.valueOf(str[6]));
				list.add(new Instruction(opCode, a1, a2, a3));
			}

			reader.close();

			return list;

		} catch (Exception e) {
			throw e;
		}
	}

	public Random getRandom() {
		return RANDOM;
	}

	public PipelineHandler getPipelineHandler() {
		return pipelineHandler;
	}
}
