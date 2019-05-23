package io.github.gfanedm.machine.memory;

public class MemoryChecker {

	private final MemoryHandler memoryHandler;

	public MemoryChecker(MemoryHandler memoryHandler) {
		this.memoryHandler = memoryHandler;
	}

	public MemoryBlock memorySearch(MemoryAddress address) {
		int posCache1 = address.getBlock() % memoryHandler.getCacheMemory().maxSize();
		int posCache2 = address.getBlock() % memoryHandler.getSlaveMemory().maxSize();

		int cost = 0;

		MemoryBlock cache = memoryHandler.getCacheMemory().get(posCache1);
		MemoryBlock slave = memoryHandler.getSlaveMemory().get(posCache2);

		if (cache.getAddress() == address.getBlock()) {

			cost += 10;
			cache.setCost(cost);
			cache.setHit(1);
			cache.setUpdate();

			return memoryHandler.getCacheMemory().replace(posCache1, cache);

		} else if (slave.getAddress() == address.getBlock()) {
			cost += 110;

			slave.setHit(2);
			slave.setUpdate();

			return testCaches(posCache1, posCache2, cost);
		} else {

			cost += 1110;

			if (!slave.isUpdate()) {
				memoryHandler.getSlaveMemory().replace(posCache2, memoryHandler.getMemory().get(address.getBlock()));
				memoryHandler.getSlaveMemory().get(posCache2).setHit(3);
				
				return testCaches(posCache1, posCache2, cost);
			} else {
				
				memoryHandler.getMemory().replace(slave.getAddress(), slave);
				memoryHandler.getSlaveMemory().get(posCache2).setUpdate();
				
				memoryHandler.getSlaveMemory().replace(posCache2, memoryHandler.getMemory().get(address.getBlock()));
				memoryHandler.getSlaveMemory().get(posCache2).setHit(3);
				return testCaches(posCache1, posCache2, cost);
			}
		}

	}

	private MemoryBlock testCaches(int cachePos, int slavePos, int cost) {
		if (!memoryHandler.getCacheMemory().get(cachePos).isUpdate()) {
			memoryHandler.getCacheMemory().replace(cachePos, memoryHandler.getSlaveMemory().get(slavePos));
		} else {
			MemoryBlock aux = memoryHandler.getCacheMemory().get(cachePos);
			memoryHandler.getCacheMemory().replace(cachePos, memoryHandler.getSlaveMemory().get(slavePos));
			memoryHandler.getSlaveMemory().replace(slavePos, aux);
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
