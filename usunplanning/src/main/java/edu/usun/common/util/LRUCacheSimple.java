package edu.usun.common.util;

import java.util.*;

/**
 * Simple in-memory LRU cache. Not thread safe.
 * When max capacity is exceeded the oldest accessed records in the cache is removed.
 * 
 * @author usun
 *
 * @param <K> Key type. Should have hashCode and equals methods properly implemented. See requirements for keys under java.util.LinkedHashMap
 * @param <V> Value type.
 */
public class LRUCacheSimple<K, V> extends LinkedHashMap<K, V> {

	/** Version for serialization. */
	private static final long serialVersionUID = 1;
	
	/** Maximum capacity for LRU cache. When reached, the oldest accessed record is removed. */
	private int maxCapacity;
	
	public LRUCacheSimple(int maxCapacity) {
		super(maxCapacity, 1.0f, true);
		this.maxCapacity = maxCapacity;
	}
	
	/**
	 * Ensure max limit of the LRU cache is enforced and eldest (in terms of access - put/get operations) 
	 * record is removed from the map/cache.
	 * @param eldestEntry The eldest (in term of put/get operations used over it) record so far. 
	 * @return Indicates if the eldest record should be removed from this map/cache.
	 */
	@Override
	protected boolean removeEldestEntry(Map.Entry<K,V> eldestEntry) {
		return this.maxCapacity > 0 && size() > this.maxCapacity;
	}
	
	/**
	 * @return The maximum capacity of the LRU cache.
	 */
	public int getMaxCapacity() {
		return this.maxCapacity;
	}
}
