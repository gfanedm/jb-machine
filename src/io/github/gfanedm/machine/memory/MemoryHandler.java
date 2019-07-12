package io.github.gfanedm.machine.memory;

import io.github.gfanedm.machine.map.SizeableList;

public class MemoryHandler {

	private int lines, columns;
	private final SizeableList<MemoryBlock> DATA_MEMORY;
	private final SizeableList<MemoryBlock> CACHE_MEMORY;
	private final SizeableList<MemoryBlock> SECONDARY_CACHE_MEMORY;
	private final SizeableList<MemoryBlock> HARD_DISK_MEMORY;
	private final SizeableList<MemoryBlock> USE_LIST;
	private final HardDisk hardDisk;

	public MemoryHandler(int ram, int cacheSize, int secondaryCacheSize, int wordsSize, int hdSize, String fileName)
			throws Exception {
		this.lines = ram;

		if (ram <= 0 || cacheSize <= 0 || secondaryCacheSize <= 0 || wordsSize <= 0) {
			throw new Exception("Invalid size of the memory");
		}

		this.DATA_MEMORY = new SizeableList<MemoryBlock>(ram);

		for (int i = 0; i < ram; i++) {
			this.DATA_MEMORY.add(i, new MemoryBlock(i, wordsSize));
		}

		this.CACHE_MEMORY = new SizeableList<MemoryBlock>(cacheSize);

		for (int i = 0; i < cacheSize; i++) {
			this.CACHE_MEMORY.add(i, new MemoryBlock(i, wordsSize));
		}

		this.SECONDARY_CACHE_MEMORY = new SizeableList<MemoryBlock>(secondaryCacheSize);

		for (int i = 0; i < secondaryCacheSize; i++) {
			this.SECONDARY_CACHE_MEMORY.add(i, new MemoryBlock(i, wordsSize));
		}

		this.HARD_DISK_MEMORY = new SizeableList<MemoryBlock>(hdSize);

		for (int i = 0; i < ram; i++) {
			this.HARD_DISK_MEMORY.add(i, new MemoryBlock(i, wordsSize));
		}

		this.USE_LIST = new SizeableList<MemoryBlock>(3);

		for (int i = 0; i < 3; i++) {
			this.USE_LIST.add(i, new MemoryBlock(0, i));
		}

		this.hardDisk = new HardDisk(fileName);
		
	}

	public int getColumns() {
		return columns;
	}

	public int getLines() {
		return lines;
	}

	public SizeableList<MemoryBlock> getMemory() {
		return DATA_MEMORY;
	}

	public SizeableList<MemoryBlock> getCacheMemory() {
		return CACHE_MEMORY;
	}

	public SizeableList<MemoryBlock> getSecondMemory() {
		return SECONDARY_CACHE_MEMORY;
	}

	public SizeableList<MemoryBlock> getUseList() {
		return USE_LIST;
	}

	public HardDisk getHardDisk() {
		return hardDisk;
	}

	public void setList(MemoryBlock... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			getUseList().set(i, blocks[i]);
		}
	}

	public enum MemoryType {
		HARDDISK(4), RAM(3), CACHE(1), SECOND(2), INVALID(-1);

		int type;

		private MemoryType(int type) {
			this.type = type;

		}

		public int getType() {
			return type;
		}
	}
}
