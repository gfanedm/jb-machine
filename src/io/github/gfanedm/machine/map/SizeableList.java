package io.github.gfanedm.machine.map;

import java.util.ArrayList;

import javax.naming.SizeLimitExceededException;

public class SizeableList<V> extends ArrayList<V> {

	private static final long serialVersionUID = 1L;

	private int maxSize;

	public SizeableList() {
		super();
		this.maxSize = Integer.MAX_VALUE;
	}

	public SizeableList(int maxSize) {
		super();

		this.maxSize = maxSize;
	}

	@Override
	public boolean add(V element) {
		try {
			if (size() >= maxSize && !contains(element)) {
				throw new SizeLimitExceededException("The orignal size is " + maxSize);
			} else {
				super.add(element);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return super.add(element);
	}
	
	public int getMaxSize() {
		return maxSize;
	}
}
