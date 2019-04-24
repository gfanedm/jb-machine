package io.github.gfanedm.machine.instruction;

import io.github.gfanedm.machine.map.SizeableList;

public class Instruction {

	private final SizeableList<Integer> list;

	public Instruction(Integer... values) {
		this.list = new SizeableList<Integer>(values.length);

		if (values.length == 0) {
			return;
		}
		for (int i : values) {
			list.add(i);
		}
	}

	public int get(int position) {
		return list.get(position);
	}

}
