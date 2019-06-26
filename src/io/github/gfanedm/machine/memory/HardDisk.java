package io.github.gfanedm.machine.memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileAlreadyExistsException;

public class HardDisk {

	public final File file;

	public HardDisk(String fileName) throws Exception {
		this.file = new File(fileName);

		if (!file.canRead() && !file.canWrite()) {
			throw new Exception("The hard disk file don't have the permissions needed.");
		}

		if (file.isDirectory()) {
			throw new FileAlreadyExistsException("The hard disk file is a directory");
		}

		if (!file.exists()) {
			file.createNewFile();
		}
	}

	public boolean write(byte[] str) {
		try {
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(str);
			stream.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public byte[] read() {
		try {
			FileInputStream stream = new FileInputStream(file);

			byte[] data = new byte[(int) file.length()];
			stream.read(data, 0, data.length);
			stream.close();

			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
