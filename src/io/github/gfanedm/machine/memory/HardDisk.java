package io.github.gfanedm.machine.memory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.gfanedm.machine.map.SizeableList;

public class HardDisk {

	private static final MemoryBlock SAMPLE = new MemoryBlock(Integer.MAX_VALUE, 4);
	private static final byte[] serialized = SAMPLE.serialize();

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

	public boolean generate(int size, int words) {

		List<MemoryBlock> blocks = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			blocks.add(new MemoryBlock(i, words));
		}

		return write(blocks);

	}

	public boolean write(Collection<MemoryBlock> blocks) {

		try {
			file.delete();
			file.createNewFile();

			int a = 0;
			int lastSize = 0;
			for (MemoryBlock block : blocks) {
				byte[] bl = block.serialize();
				if (lastSize == 0) {
					lastSize = bl.length;
				}

				boolean changed = false;

				if (lastSize != bl.length) {
					for (int i = 0; i < 3; i++) {
						System.out.println("TAMANHODIF = " + a + " | TAM = " + bl.length + " | TAM S = "
								+ serialized.length + " | bytes = " + a * bl.length + " : "
								+ ((a * bl.length) + serialized.length));

						block = SAMPLE;
					}
					changed = true;
				}

				writeOne(block, a);

				if (changed) {
					System.out.println("TAMANHO file= " + file.length());
					System.out.println("______________________________________");
				}

				a++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean writeOne(MemoryBlock block, int pos) {
		pos = pos * serialized.length;
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
		pos = pos * SAMPLE.serialize().length;
		if (pos >= file.length()) {
			System.out.println("Tentou ler mais do que deveria.");
			return null;
		} else {

			try {
				byte[] reciever = new byte[SAMPLE.serialize().length];

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
	}

	public boolean read(SizeableList<MemoryBlock> hardDisk) {
		try {
			MemoryBlock block;
			for (int i = 0; i < hardDisk.getMaxSize(); i++) {
				// System.out.println("lendo : " + i + "/" + hardDisk.getMaxSize());
				// System.out.println("Tam : " + (i * SAMPLE.serialize().length));

				block = readOne(i);
				if (block != null) {
					// System.out.println("Block readed " + block.toString());
					hardDisk.set(i, block);
				}
				// System.out.println("----------------------");
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
