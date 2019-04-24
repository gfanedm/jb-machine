package io.github.gfanedm.machine.map;

import java.util.HashMap;

import javax.naming.SizeLimitExceededException;

public class SizeableHashMap<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = 1L;
	
    private int maxSize;
    
    public SizeableHashMap() {
		super();
		this.maxSize = Integer.MAX_VALUE;
	}
    
    public SizeableHashMap(int maxSize) {
        super();
        
        this.maxSize = maxSize;
    }

	public V put(K key, V value) {
        try {
        	if (size() >= maxSize && !containsKey(key)) {
        		throw new SizeLimitExceededException("The orignal size is " + maxSize);
        	}else {
        		return super.put(key, value);
        	}
		}catch (Exception exception) {
			exception.printStackTrace();
		}
		return (V) null;
    }
  }
