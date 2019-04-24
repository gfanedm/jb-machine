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

	public boolean add(V value) {
        try {
        	if (size() >= maxSize) {
        		throw new SizeLimitExceededException("The orignal size is " + maxSize);
        	}else {
        		return super.add(value);
        	}
		}catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
    }
  }
