package io.github.gfanedm.machine.memory;

import java.util.HashMap;

public class MemoryChecker {

	private final MemoryHandler memoryHandler;

	public MemoryChecker(MemoryHandler memoryHandler) {
		this.memoryHandler = memoryHandler;
	}

	public MemoryBlock memorySearch(MemoryAddress address) {
		int posCache1 = getAddress(memoryHandler.getCacheMemory(), address);
		int posCache2 = getAddress(memoryHandler.getSecondMemory(), address);
		int posRam = getAddress(memoryHandler.getHardDiskMemory(), address);

		int cost = 0;

		if (posCache1 != -1) {

			cost += 10;
			memoryHandler.getCacheMemory().get(posCache1).incrementTimes();
			memoryHandler.getCacheMemory().get(posCache1).setCost(cost);
			memoryHandler.getCacheMemory().get(posCache1).setHit(1);
			memoryHandler.getCacheMemory().get(posCache1).setUpdate();

			memoryHandler.getCacheMemory().replace(posCache1, memoryHandler.getCacheMemory().get(posCache1));
			return memoryHandler.getCacheMemory().get(posCache1);

		} else if (posCache2 != -1) {
			int posLeast = getLeastRecentlyUsed(memoryHandler.getCacheMemory());

			cost += 110;
			memoryHandler.getSecondMemory().get(posCache2).incrementTimes();
			memoryHandler.getSecondMemory().get(posCache2).setHit(2);
			memoryHandler.getSecondMemory().get(posCache2).setUpdate();
			memoryHandler.getSecondMemory().get(posCache2).setCost(cost);
			return testCaches(posLeast, posCache2, cost);
		} else if (posRam != -1) {

			cost += 1110;
			return exchangeRam(cost, address);
		} else {
			cost += 11110;

			int posLeastRam = getLeastRecentlyUsed(memoryHandler.getMemory());

			if (!memoryHandler.getMemory().get(posLeastRam).isUpdate()) {
				memoryHandler.getMemory().replace(posLeastRam,
						memoryHandler.getHardDiskMemory().get(address.getBlock()));
				memoryHandler.getMemory().get(posLeastRam).incrementTimes();
				memoryHandler.getMemory().get(posLeastRam).setHit(3);
				return memoryHandler.getMemory().get(posLeastRam);
			} else {
				memoryHandler.getHardDiskMemory().replace(memoryHandler.getMemory().get(posLeastRam).getAddress(),
						memoryHandler.getMemory().get(posLeastRam));
				memoryHandler.getHardDisk().writeOne(memoryHandler.getHardDiskMemory().get(posLeastRam), posLeastRam);
				memoryHandler.getMemory().get(posLeastRam).incrementTimes();
				memoryHandler.getMemory().get(posLeastRam).setUpdate();
				memoryHandler.getMemory().replace(posLeastRam, memoryHandler.getHardDiskMemory().get(address.getBlock()));
				memoryHandler.getMemory().get(posLeastRam).setHit(4);
				return exchangeRam(cost, address);
			}
		}
	}

	public MemoryBlock exchangeRam(int cost, MemoryAddress address){

		int posLeastCache = getLeastRecentlyUsed(memoryHandler.getCacheMemory());
		int posLeastSecond = getLeastRecentlyUsed(memoryHandler.getSecondMemory());
		
		if (!memoryHandler.getSecondMemory().get(posLeastSecond).isUpdate()) {
			memoryHandler.getSecondMemory().replace(posLeastSecond,
					memoryHandler.getMemory().get(address.getBlock()));
			memoryHandler.getSecondMemory().get(posLeastSecond).incrementTimes();
			memoryHandler.getSecondMemory().get(posLeastSecond).setHit(3);
			return testCaches(posLeastCache, posLeastSecond, cost);
		} else {

			memoryHandler.getMemory().replace(memoryHandler.getSecondMemory().get(posLeastSecond).getAddress(),
					memoryHandler.getSecondMemory().get(posLeastSecond));
			memoryHandler.getSecondMemory().get(posLeastSecond).incrementTimes();
			memoryHandler.getSecondMemory().get(posLeastSecond).setUpdate();
			memoryHandler.getSecondMemory().replace(posLeastSecond,
					memoryHandler.getMemory().get(address.getBlock()));
			memoryHandler.getSecondMemory().get(posLeastSecond).setHit(3);
			return testCaches(posLeastCache, posLeastSecond, cost);
		}
	}
	
	private Integer getAddress(HashMap<Integer, MemoryBlock> cache, MemoryAddress address) {
		return cache.keySet().stream().mapToInt(i -> i).filter(i -> cache.get(i).getAddress() == address.getBlock())
				.findFirst().orElse(-1);
	}

	private Integer getLeastRecentlyUsed(HashMap<Integer, MemoryBlock> cache) {
		int low = Integer.MAX_VALUE;
		int pos = 0;

		for (int i = 0; i < cache.size(); i++) {
			if (low > cache.get(i).getTimes()) {
				low = cache.get(i).getTimes();
				pos = i;
			}
		}
		return pos;
	}

	private MemoryBlock testCaches(int cachePos, int secondPos, int cost) {

		System.out.println("C = " + cachePos);
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
