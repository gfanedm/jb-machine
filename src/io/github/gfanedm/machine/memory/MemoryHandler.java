package io.github.gfanedm.machine.memory;

import io.github.gfanedm.machine.map.SizeableHashMap;

public class MemoryHandler {

	private int lines, columns;
	private final SizeableHashMap<Integer, MemoryBlock> DATA_MEMORY;
	private final SizeableHashMap<Integer, MemoryBlock> CACHE_MEMORY;
	private final SizeableHashMap<Integer, MemoryBlock> SECONDARY_CACHE_MEMORY;
	private final SizeableHashMap<Integer, MemoryBlock> USE_LIST;

	public MemoryHandler(int ram, int cacheSize, int secondaryCacheSize, int wordsSize) throws Exception {
		this.lines = ram;

		if (ram <= 0 || cacheSize <= 0 || secondaryCacheSize <= 0 || wordsSize <= 0) {
			throw new Exception("Invalid size of the memory");
		}

		this.DATA_MEMORY = new SizeableHashMap<Integer, MemoryBlock>(ram);

		for (int i = 0; i < ram; i++) {
			this.DATA_MEMORY.put(i, new MemoryBlock(i, wordsSize, MemoryType.RAM));
		}

		this.CACHE_MEMORY = new SizeableHashMap<Integer, MemoryBlock>(cacheSize);

		for (int i = 0; i < cacheSize; i++) {
			this.CACHE_MEMORY.put(i, new MemoryBlock(i, wordsSize, MemoryType.CACHE));
		}

		this.SECONDARY_CACHE_MEMORY = new SizeableHashMap<Integer, MemoryBlock>(secondaryCacheSize);

		for (int i = 0; i < secondaryCacheSize; i++) {
			this.SECONDARY_CACHE_MEMORY.put(i, new MemoryBlock(i, wordsSize, MemoryType.SECOND));
		}

		this.USE_LIST = new SizeableHashMap<Integer, MemoryBlock>(3);

		for (int i = 0; i < 3; i++) {
			this.USE_LIST.put(i, new MemoryBlock(0, i, MemoryType.INVALID));
		}
	}

	public int getColumns() {
		return columns;
	}

	public int getLines() {
		return lines;
	}

	public SizeableHashMap<Integer, MemoryBlock> getMemory() {
		return DATA_MEMORY;
	}

	public SizeableHashMap<Integer, MemoryBlock> getCacheMemory() {
		return CACHE_MEMORY;
	}

	public SizeableHashMap<Integer, MemoryBlock> getSecondMemory() {
		return SECONDARY_CACHE_MEMORY;
	}

	public SizeableHashMap<Integer, MemoryBlock> getUseList() {
		return USE_LIST;
	}

	public void setList(MemoryBlock... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			getUseList().replace(i, blocks[i]);
		}
	}

	public enum MemoryType {
		RAM(3), CACHE(1), SECOND(2), INVALID(-1);
		
		int type;

		private MemoryType(int type) {
			this.type = type;

		}

		public int getType() {
			return type;
		}
	}
}
