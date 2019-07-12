package io.github.gfanedm.machine.instructions;

import io.github.gfanedm.machine.map.SizeableList;
import io.github.gfanedm.machine.memory.MemoryChecker.MemoryAddress;

public class Instruction {

	private final SizeableList<MemoryAddress> list;
	private final int opCode;

	public Instruction(int opCode, MemoryAddress... values) {
		this.opCode = opCode;
		this.list = new SizeableList<MemoryAddress>(values.length);

		if (values.length == 0) {
			return;
		}
		for (int i = 0; i < values.length; i++) {
			list.add(i, values[i]);
		}
	}
	
	public int getOpCode() {
		return opCode;
	}

	public SizeableList<MemoryAddress> getList() {
		return list;
	}

}
