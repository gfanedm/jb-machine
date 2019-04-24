package io.github.gfanedm.machine.memory;

import java.util.Random;

import io.github.gfanedm.machine.map.SizeableHashMap;

public class MemoryHandler {

	private int lines;
	private final SizeableHashMap<Integer, Integer> DATA_MEMORY;

	public MemoryHandler(int lines) throws Exception {
		this.lines = lines;
		
		if(lines <= 0) {
			throw new Exception("Invalid size of the memory");
		}

		this.DATA_MEMORY = new SizeableHashMap<Integer, Integer>();

		Random random = new Random();
		for (int i = 0; i < lines; i++) {
			this.DATA_MEMORY.put(i, random.nextInt(10000));
		}
	}

	public int getLines() {
		return lines;
	}

	public SizeableHashMap<Integer, Integer> getMemory() {
		return DATA_MEMORY;
	}
}
