package io.github.gfanedm.machine.memory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import io.github.gfanedm.machine.map.SizeableHashMap;
import io.github.gfanedm.machine.memory.MemoryHandler.MemoryType;

public class MemoryBlock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final SizeableHashMap<Integer, Integer> words;
	private int address;
	private boolean update;
	private int cost, hit;
	private int times;
	private MemoryType memoryType;

	public MemoryBlock(int address, int size, MemoryType memoryType) {
		this.words = new SizeableHashMap<Integer, Integer>(size);

		Random random = new Random();
		for (int i = 0; i < size; i++) {
			this.words.put(i, random.nextInt(1000));
		}

		this.address = address;
		this.update = false;
		this.cost = 0;
		this.hit = 0;
		this.times = 0;
		this.memoryType = memoryType;
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

	public int getTimes() {
		return times;
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

	public void incrementTimes() {
		this.times += 1;
	}

	public void printTable() {
		System.out.println("--------------------");
		for (int j = 0; j < words.size(); j++) {
			System.out.println(j + "|" + words.get(j));
		}
		System.out.println("--------------------");
	}

	public MemoryType getMemoryType() {
		return memoryType;
	}
	
	public String toString() {
		return String.format("{%d, %b, %d, %d}", address, update, cost, hit, times);
	}

	public byte[] serialize() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(this);
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static MemoryBlock deserialize(byte[] data) {
		try {
			return (MemoryBlock) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
