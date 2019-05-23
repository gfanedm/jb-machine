package io.github.gfanedm.machine;

import java.util.List;

import io.github.gfanedm.machine.instructions.Instruction;
import io.github.gfanedm.machine.memory.MemoryBlock;
import io.github.gfanedm.machine.memory.MemoryChecker;
import io.github.gfanedm.machine.memory.MemoryHandler;
import io.github.gfanedm.machine.pipeline.PipelineHandler;
import io.github.gfanedm.machine.program.ProgramFactory;

public class Machine {

	public static final int RAM_SIZE = 64;
	public static final int CACHE_SIZE = 16;
	public static final int SLAVE_SIZE = 32;
	public static final int MACHINE_SIZE = 100000;
	public static final int WORDS_SIZE = 4;

	private int cacheMiss = 0, cacheHit = 0, slaveMiss = 0, slaveHit = 0, ramMiss = 0, ramHit = 0, cost = 0;

	public MemoryHandler memoryHandler;
	public ProgramFactory programFactory;
	public PipelineHandler pipelineHandler;
	public MemoryChecker memoryChecker;

	public static void main(String[] args) {
		new Machine();
	}

	public Machine() {

		int opcode = 0, pc = 0;
		try {

			this.memoryHandler = new MemoryHandler(RAM_SIZE, CACHE_SIZE, SLAVE_SIZE, WORDS_SIZE);
			this.pipelineHandler = new PipelineHandler();
			this.memoryChecker = new MemoryChecker(memoryHandler);

			this.programFactory = new ProgramFactory(pipelineHandler);

			List<Instruction> ins = programFactory.fileProgram("instrucoes.txt");

			while (opcode != -1) {

				Instruction instruction = ins.get(pc);

				opcode = instruction.getOpCode();

				MemoryBlock data1 = memoryChecker.memorySearch(instruction.getList().get(0)),
						data2 = memoryChecker.memorySearch(instruction.getList().get(1)),
						data3 = memoryChecker.memorySearch(instruction.getList().get(2));

				cost += (data1.getCost() + data2.getCost() + data3.getCost());

				addHit(data1, data2, data3);
				memoryHandler.setList(data1, data2, data3);

				System.out.println("\nCusto ate o momento do programa em execucao: " + cost);
				System.out.println("C-HIT\t|C-MISS\t|S-HIT\t|S-MISS\t|R-HIT\t|R-MISS\n" + cacheHit + "\t|" + cacheMiss
						+ "\t|" + slaveHit + "\t|" + slaveMiss + "\t|" + ramHit + "\t|" + ramMiss);

				pipelineHandler.execute(instruction, memoryHandler);

				pc++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		double total = cacheHit + slaveHit + ramHit;

		System.out.println("\nCusto total: " + cost);
		System.out.println("C-HIT\t|C-MISS\t|S-HIT\t|S-MISS\t|R-HIT\t|R-MISS\n" + cacheHit + "\t|" + cacheMiss + "\t|"
				+ slaveHit + "\t|" + slaveMiss + "\t|" + ramHit + "\t|" + ramMiss);
		System.out.println("Taxa C1 = " + (cacheHit * 100 / total) + "%");
		System.out.println("Taxa C2 = " + (slaveHit * 100 / total) + "%");
		System.out.println("Taxa RAM = " + (ramHit * 100 / total) + "%");
		System.out.println("Total: " + total);
	}

	public void addHit(MemoryBlock... blocks) {
		for (MemoryBlock block : blocks) {
			if (block.getHit() == 1) {
				cacheHit++;
			} else if (block.getHit() == 2) {
				cacheMiss++;
				ramMiss++;
				slaveHit++;
			} else if (block.getHit() == 3) {
				cacheMiss++;
				slaveMiss++;
				ramHit++;
			}
		}
	}

}
