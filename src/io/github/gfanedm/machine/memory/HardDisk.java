package io.github.gfanedm.machine.memory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;

import io.github.gfanedm.machine.map.SizeableHashMap;
import io.github.gfanedm.machine.memory.MemoryHandler.MemoryType;

public class HardDisk {

	private static final MemoryBlock SAMPLE = new MemoryBlock(0, 4, MemoryType.HARDDISK);

	private static final int MEMORY_SIZE = SAMPLE.serialize().length;

	public final File file;

	public HardDisk(String fileName) throws Exception {

		this.file = new File(fileName);

		if (!file.exists()) {
			file.createNewFile();
		} else {
			if (!file.canRead() || !file.canWrite()) {
				throw new Exception("The hard disk file don't have the permissions needed.");
			}

			if (file.isDirectory()) {
				throw new FileAlreadyExistsException("The hard disk file is a directory");
			}
		}
	}

	public boolean write(MemoryBlock[] blocks) {
		try {
			int a = 0;
			for (MemoryBlock block : blocks) {
				writeOne(block, a * block.serialize().length);
				a++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean writeOne(MemoryBlock block, int pos) {
		try {
			RandomAccessFile random = new RandomAccessFile(file, "rw");
			random.skipBytes(pos);
			random.write(block.serialize());
			random.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public MemoryBlock readOne(int pos) {
		try {
			byte[] reciever = SAMPLE.serialize();

			RandomAccessFile random = new RandomAccessFile(file, "rw");
			random.skipBytes(pos);
			random.read(reciever);
			random.close();

			return MemoryBlock.deserialize(reciever);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean read(SizeableHashMap<Integer, MemoryBlock> hardDisk) {
		try {
			MemoryBlock block;
			for (int i = 0; i < hardDisk.maxSize(); i++) {
				if ((block = readOne(i * MEMORY_SIZE)) != null) {
					hardDisk.put(i, block);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
