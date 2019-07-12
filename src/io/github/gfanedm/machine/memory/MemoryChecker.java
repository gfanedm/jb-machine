package io.github.gfanedm.machine.memory;

import java.util.stream.IntStream;

import io.github.gfanedm.machine.map.SizeableList;

public class MemoryChecker {

	private final MemoryHandler memoryHandler;

	public MemoryChecker(MemoryHandler memoryHandler) {
		this.memoryHandler = memoryHandler;
	}

	public MemoryBlock memorySearch(MemoryAddress address) {
		int posCache1 = getAddress(memoryHandler.getCacheMemory(), address);
		int posCache2 = getAddress(memoryHandler.getSecondMemory(), address);
		int posRam = getAddress(memoryHandler.getMemory(), address);

		int cost = 0;

		if (posCache1 != -1) {

			cost += 10;
			memoryHandler.getCacheMemory().get(posCache1).incrementTimes();
			memoryHandler.getCacheMemory().get(posCache1).setCost(cost);
			memoryHandler.getCacheMemory().get(posCache1).setHit(1);
			memoryHandler.getCacheMemory().get(posCache1).setUpdate();

			memoryHandler.getCacheMemory().set(posCache1, memoryHandler.getCacheMemory().get(posCache1));
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
			return exchangeRam(cost, address, address.getBlock(), 3);
		} else {
			cost += 11110;
			// pegar o menor prioridade da ram
			// verifica se o bloco foi atualizado
			// se foi acha no hd e atualizar
			// pesquisa do endereco nao
			int posLeastRam = getLeastRecentlyUsed(memoryHandler.getMemory());

			if (memoryHandler.getMemory().get(posLeastRam).isUpdate()) {

				MemoryBlock blockLfu = memoryHandler.getHardDisk().readOne(posLeastRam);
				memoryHandler.getHardDisk().writeOne(blockLfu, posLeastRam);

				MemoryBlock block = memoryHandler.getHardDisk().readOne(address.getBlock());
				memoryHandler.getMemory().set(posLeastRam, block);
				memoryHandler.getMemory().get(posLeastRam).setHit(4);
				memoryHandler.getMemory().get(posLeastRam).setUpdate();
				memoryHandler.getMemory().get(posLeastRam).setCost(cost);

				return exchangeRam(cost, address, posLeastRam, 4);
			} else {

				MemoryBlock block = memoryHandler.getHardDisk().readOne(address.getBlock());
				memoryHandler.getMemory().set(posLeastRam, block);
				memoryHandler.getMemory().get(posLeastRam).setHit(4);
				memoryHandler.getMemory().get(posLeastRam).setUpdate();
				memoryHandler.getMemory().get(posLeastRam).setCost(cost);

				return exchangeRam(cost, address, posLeastRam, 4);
			}

		}
	}

	public MemoryBlock exchangeRam(int cost, MemoryAddress address, int block, int hit) {

		int posLeastCache = getLeastRecentlyUsed(memoryHandler.getCacheMemory());
		int posLeastSecond = getLeastRecentlyUsed(memoryHandler.getSecondMemory());

		if (!memoryHandler.getSecondMemory().get(posLeastSecond).isUpdate()) {
			memoryHandler.getSecondMemory().set(posLeastSecond, memoryHandler.getMemory().get(block));
			memoryHandler.getSecondMemory().get(posLeastSecond).incrementTimes();
			memoryHandler.getSecondMemory().get(posLeastSecond).setHit(hit);
			return testCaches(posLeastCache, posLeastSecond, cost);
		} else {
			memoryHandler.getMemory().set(posLeastSecond, memoryHandler.getSecondMemory().get(posLeastSecond));
			memoryHandler.getSecondMemory().get(posLeastSecond).incrementTimes();
			memoryHandler.getSecondMemory().get(posLeastSecond).setUpdate();
			memoryHandler.getSecondMemory().set(posLeastSecond,	memoryHandler.getMemory().get(posLeastCache));
			memoryHandler.getSecondMemory().get(posLeastSecond).setHit(hit);
			return testCaches(posLeastCache, posLeastSecond, cost);
		}
	}

	private Integer getAddress(SizeableList<MemoryBlock> cache, MemoryAddress address) {
		return IntStream.range(0, cache.size()).filter(i -> cache.get(i).getAddress() == address.getBlock()).findFirst()
				.orElse(-1);
	}

	private Integer getLeastRecentlyUsed(SizeableList<MemoryBlock> cache) {
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

		// System.out.println("C = " + cachePos);
		if (!memoryHandler.getCacheMemory().get(cachePos).isUpdate()) {
			memoryHandler.getCacheMemory().set(cachePos, memoryHandler.getSecondMemory().get(secondPos));
		} else {
			MemoryBlock aux = memoryHandler.getCacheMemory().get(cachePos);
			memoryHandler.getCacheMemory().set(cachePos, memoryHandler.getSecondMemory().get(secondPos));
			memoryHandler.getSecondMemory().set(secondPos, aux);
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
