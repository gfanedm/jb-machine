package io.github.gfanedm.machine.memory;

import java.util.HashMap;

public class MemoryChecker {

	private final MemoryHandler memoryHandler;

	public MemoryChecker(MemoryHandler memoryHandler) {
		this.memoryHandler = memoryHandler;
	}
	

	public MemoryBlock memorySearch(MemoryAddress address) {
		int posCache1 = address.getBlock() % memoryHandler.getCacheMemory().size();
		int posCache2 = address.getBlock() % memoryHandler.getSecondMemory().size();

		int cost = 0;

		if (memoryHandler.getCacheMemory().get(posCache1).getAddress() == address.getBlock()) {

			cost += 10;
			memoryHandler.getCacheMemory().get(posCache1).incrementTimes();
			memoryHandler.getCacheMemory().get(posCache1).setCost(cost);
			memoryHandler.getCacheMemory().get(posCache1).setHit(1);
			memoryHandler.getCacheMemory().get(posCache1).setUpdate();
			
			memoryHandler.getCacheMemory().replace(posCache1, memoryHandler.getCacheMemory().get(posCache1));
			
			return memoryHandler.getCacheMemory().get(posCache1);

		} else if (memoryHandler.getSecondMemory().get(posCache2).getAddress() == address.getBlock()) {
			int posLeast = getLeastRecentlyUsed(memoryHandler.getCacheMemory());

			cost += 110;
			memoryHandler.getSecondMemory().get(posCache2).incrementTimes();
			memoryHandler.getSecondMemory().get(posCache2).setHit(2);
			memoryHandler.getSecondMemory().get(posCache2).setUpdate();
			
			return testCaches(posLeast, posCache2, cost);
		} else {

			cost += 1110;

			int posLeastCache = getLeastRecentlyUsed(memoryHandler.getCacheMemory());
			int posLeastSecond = getLeastRecentlyUsed(memoryHandler.getSecondMemory());


			if (!memoryHandler.getSecondMemory().get(posLeastSecond).isUpdate()) {
				memoryHandler.getSecondMemory().replace(posLeastSecond,	memoryHandler.getMemory().get(address.getBlock()));
				memoryHandler.getSecondMemory().get(posLeastSecond).incrementTimes();
				memoryHandler.getSecondMemory().get(posLeastSecond).setHit(3);

				return testCaches(posLeastCache, posLeastSecond, cost);
			} else {

				memoryHandler.getMemory().replace(memoryHandler.getSecondMemory().get(posLeastSecond).getAddress(), memoryHandler.getSecondMemory().get(posLeastSecond));
				memoryHandler.getSecondMemory().get(posLeastSecond).incrementTimes();
				memoryHandler.getSecondMemory().get(posLeastSecond).setUpdate();
				memoryHandler.getSecondMemory().replace(posLeastSecond,	memoryHandler.getMemory().get(address.getBlock()));
				memoryHandler.getSecondMemory().get(posLeastSecond).setHit(3);
				return testCaches(posLeastCache, posLeastSecond, cost);
			}
		}
	}

	private Integer getLeastRecentlyUsed(HashMap<Integer, MemoryBlock> cache) {
		int low = Integer.MAX_VALUE;
		int pos = 0;

		for (int i = 0; i < cache.size(); i++) {
			if(low > cache.get(i).getTimes()) {
				low = cache.get(i).getTimes();
				pos = i;
			}
		}
		return pos;
	}

	private MemoryBlock testCaches(int cachePos, int secondPos, int cost) {
		if (!memoryHandler.getCacheMemory().get(cachePos).isUpdate()) {
			memoryHandler.getCacheMemory().replace(cachePos, memoryHandler.getSecondMemory().get(secondPos));
		} else {
			MemoryBlock aux = memoryHandler.getCacheMemory().get(cachePos);
			memoryHandler.getCacheMemory().replace(cachePos, memoryHandler.getSecondMemory().get(secondPos));
			memoryHandler.getSecondMemory().replace(secondPos, aux);
		}

		memoryHandler.getCacheMemory().get(cachePos).setCost(cost);
		
		return memoryHandler.getCacheMemory().get(cachePos);
	}

	public static class MemoryAddress {

		private int block, word;

		public MemoryAddress(int block, int word) {
			this.block = block;
			this.word = word;
		}

		public int getBlock() {
			return block;
		}

		public int getWord() {
			return word;
		}
	}
}
