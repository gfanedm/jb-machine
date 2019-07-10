package io.github.gfanedm.machine;

import java.util.List;

import io.github.gfanedm.machine.instructions.Instruction;
import io.github.gfanedm.machine.interruption.Interruption;
import io.github.gfanedm.machine.memory.MemoryBlock;
import io.github.gfanedm.machine.memory.MemoryChecker;
import io.github.gfanedm.machine.memory.MemoryHandler;
import io.github.gfanedm.machine.pipeline.PipelineHandler;
import io.github.gfanedm.machine.program.ProgramFactory;

public class Machine implements Runnable {

	public static final int CACHE_SIZE = 16;
	public static final int SECONDARY_SIZE = 32;
	public static final int RAM_SIZE = 64;
	public static final int WORDS_SIZE = 4;
	public static final String HARD_DISK_FILE = "hd.bin";

	private int cacheMiss = 0, cacheHit = 0, secondaryMiss = 0, secondaryHit = 0, ramHit = 0, hdHit = 0, cost = 0;

	public MemoryHandler memoryHandler;
	public ProgramFactory programFactory;
	public PipelineHandler pipelineHandler;
	public MemoryChecker memoryChecker;
	public Interruption interruption;

	public Machine(Interruption interruption) {
		this.interruption = interruption;
	}

	@Override
	public void run() {
		int opcode = 0, pc = 0;
		try {

			this.memoryHandler = new MemoryHandler(RAM_SIZE, CACHE_SIZE, SECONDARY_SIZE, WORDS_SIZE, HARD_DISK_FILE);
			this.pipelineHandler = new PipelineHandler();
			this.memoryChecker = new MemoryChecker(memoryHandler);

			this.programFactory = new ProgramFactory(pipelineHandler);

			List<Instruction> ins = programFactory.fileProgram("instrucoes.txt");

			while (interruption.isRunning() && opcode != -1) {

				Instruction instruction = ins.get(pc);

				opcode = instruction.getOpCode();

				MemoryBlock data1 = memoryChecker.memorySearch(instruction.getList().get(0)),
						data2 = memoryChecker.memorySearch(instruction.getList().get(1)),
						data3 = memoryChecker.memorySearch(instruction.getList().get(2));

				cost += (data1.getCost() + data2.getCost() + data3.getCost());

				addHit(data1, data2, data3);
				memoryHandler.setList(data1, data2, data3);
				System.out.println("==========================================");
				System.out.println("Custo ate o momento do programa em execucao: " + cost);
				System.out.println("C-HIT\t|C-MISS\t|S-HIT\t|S-MISS\t|R-HIT\n" + cacheHit + "\t|" + cacheMiss + "\t|"
						+ secondaryHit + "\t|" + secondaryMiss + "\t|" + ramHit);
				System.out.println("==========================================");
				pipelineHandler.execute(instruction, memoryHandler);

				pc++;
			}

//			System.out.println("Primeiro aaaa");
//			for (int i = 0; i < memoryHandler.getHardDiskMemory().size(); i++) {
//				memoryHandler.getHardDiskMemory().get(i).printTable();
//			}
//			System.out.println("Segundo aaaa");
//			for (int i = 0; i < memoryHandler.getMemory().size(); i++) {
//				memoryHandler.getMemory().get(i).printTable();
//			}
//
//			System.out.println("Terceiro aaaa");
//
//			memoryHandler.getHardDisk().write(memoryHandler.getMemory().values()
//					.toArray(new MemoryBlock[memoryHandler.getMemory().values().size()]));
//			System.out.println(memoryHandler.getHardDisk().readAll() + " = aaaaaaaaaaaaaaaaaaaaa");
//
//			for (int i = 0; i < memoryHandler.getHardDiskMemory().size(); i++) {
//				memoryHandler.getHardDiskMemory().get(i).printTable();
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		double total = cacheHit + secondaryHit + ramHit;
		System.out.println("==========================================");
		System.out.println("Custo total: " + cost);
		System.out.println("C-HIT\t|C-MISS\t|S-HIT\t|S-MISS\t|R-HIT\t\n" + cacheHit + "\t|" + cacheMiss + "\t|"
				+ secondaryHit + "\t|" + secondaryMiss + "\t|" + ramHit);
		System.out.println("Taxa C1 = " + (cacheHit * 100 / total) + "%");
		System.out.println("Taxa C2 = " + (secondaryHit * 100 / total) + "%");
		System.out.println("Taxa HD = " + (hdHit * 100 / total) + "%");
		System.out.println("Taxa RAM = " + (ramHit * 100 / total) + "%");
		System.out.println("Total: " + total);
		System.out.println("==========================================");
		
		interruption.setRunning(false);
	}

	public void addHit(MemoryBlock... blocks) {
		for (MemoryBlock block : blocks) {
			if (block.getHit() == 1) {
				cacheHit++;
			} else if (block.getHit() == 2) {
				cacheMiss++;
				secondaryHit++;
			} else if (block.getHit() == 3) {
				cacheMiss++;
				secondaryMiss++;
				ramHit++;
			} else if (block.getHit() == 4) {
				hdHit++;
			}
		}
	}

}
