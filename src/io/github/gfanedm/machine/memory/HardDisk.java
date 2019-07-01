package io.github.gfanedm.machine.memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collection;

public class HardDisk {

	public final MemoryHandler memoryHandler;

	public final File file;

	public HardDisk(String fileName, MemoryHandler memoryHandler) throws Exception {
		this.memoryHandler = memoryHandler;

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

	public boolean write(Collection<MemoryBlock> blocks) {
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));

			blocks.forEach(block -> {
				try {
					stream.writeObject(block);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			stream.flush();
			stream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean read() {
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));

			int i = 0;
			MemoryBlock block;
			while ((block = (MemoryBlock) stream.readObject()) != null) {
				memoryHandler.getHardDiskMemory().replace(i++, block);
			}

			stream.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean readAll() {
		try {
			int pos = 0;
			long size = 0;
			MemoryBlock block;
			while (size < file.length()) {
				block = readBlock(size);
				if (block != null) {
					memoryHandler.getHardDiskMemory().replace(pos++, block);
				}
				size++;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public MemoryBlock readBlock(long pos) {
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));

			stream.skip(pos);

			MemoryBlock memoryBlock = (MemoryBlock) stream.readObject();

			stream.close();
			return memoryBlock;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
