package io.github.gfanedm.machine.memory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import io.github.gfanedm.machine.map.SizeableList;

public class MemoryBlock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7989228035657266609L;

	private final SizeableList<Integer> words;
	private int address;
	private boolean update;
	private int cost, hit;
	private int times;

	public MemoryBlock(int address, int size) {
		this.words = new SizeableList<Integer>(size);

		Random random = new Random();
		for (int i = 0; i < size; i++) {
			this.words.add(i, random.nextInt(1000));
		}

		this.address = address;
		this.update = false;
		this.cost = 0;
		this.hit = 0;
		this.times = 0;
	}

	public SizeableList<Integer> getWords() {
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
			ByteArrayInputStream bs = new ByteArrayInputStream(data);
			ObjectInputStream is = new ObjectInputStream(bs);
			MemoryBlock block = (MemoryBlock) is.readObject();
			return block;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
