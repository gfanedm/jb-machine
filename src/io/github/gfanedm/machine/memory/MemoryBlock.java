package io.github.gfanedm.machine.memory;

import java.util.Random;

import io.github.gfanedm.machine.map.SizeableHashMap;
import io.github.gfanedm.machine.memory.MemoryHandler.MemoryType;

public class MemoryBlock {

	private final SizeableHashMap<Integer, Integer> words;
	private int address;
	private boolean update;
	private int cost, hit;

	public MemoryBlock(int address, int size, MemoryType memoryType) {
		this.words = new SizeableHashMap<Integer, Integer>(size);

		if (memoryType != MemoryType.CACHE) {
			Random random = new Random();
			for (int i = 0; i < size; i++) {
				this.words.put(i, random.nextInt(1000));
			}
		}

		this.address = address;
		this.update = false;
		this.cost = 0;
		this.hit = 0;
	}

	public SizeableHashMap<Integer, Integer> getWords() {
		return words;
	}

	public int getAddress() {
		return address;
	}

	public boolean isUpdate() {
		return update;
	}

	public int getCost() {
		return cost;
	}

	public int getHit() {
		return hit;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public void setUpdate() {
		this.update = true;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

}
