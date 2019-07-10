package io.github.gfanedm.machine;

import io.github.gfanedm.machine.map.SizeableHashMap;
import io.github.gfanedm.machine.memory.HardDisk;
import io.github.gfanedm.machine.memory.MemoryBlock;
import io.github.gfanedm.machine.memory.MemoryHandler.MemoryType;

public class TestMachine {

	public static void main(String[] args) throws Exception {
		MemoryBlock test1 = new MemoryBlock(0, 4, MemoryType.HARDDISK);
		MemoryBlock test2 = new MemoryBlock(123, 4, MemoryType.HARDDISK);
		test1.setAddress(1000);

		HardDisk hardDisk = new HardDisk("test.bin");
		hardDisk.writeOne(test1, 0);
		hardDisk.writeOne(test2, test1.serialize().length);
		MemoryBlock block = hardDisk.readOne(0);
		System.out.println(block.toString());
		
		MemoryBlock block2 = hardDisk.readOne(test1.serialize().length);
		System.out.println(block2.toString());
		
		
		SizeableHashMap<Integer,MemoryBlock> blocks = new SizeableHashMap<Integer, MemoryBlock>(2);
		hardDisk.read(blocks);
		
		blocks.values().stream().forEach(mem -> System.out.println("MEM = " + mem.toString()));
		System.out.println(blocks.size() + " = SIZE");
	}
}
