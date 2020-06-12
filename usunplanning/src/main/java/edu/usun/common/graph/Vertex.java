package edu.usun.common.graph;

/**
 * Represents a vertex for a graph.
 * @author usun
 *
 * @param <K> Key representing vertex, it will be used for comparisons with other
 * vertices for equality. Should be unique in a graph.
 * @param <V> Contains business data associated with a given graph vortex. 
 * Duplicates are generally allowed, because K key template type is used to identify the vertex.
 */
public class Vertex <K, V> {
	
	/**
	 * Key representing vertex, it will be used for comparisons with other
	 * vertices for equality. Should be unique in a graph.
	 */
	protected K key;

	/**
	 * Data value for Vertex, not related to Graph internal organization 
	 * and contains business data only.
	 */
	protected V value;
	
	/**
	 * Default empty constructor, business data is empty.
	 */
	public Vertex() {
		this(null, null);
	}
	
	/**
	 * Constructor with business data passed.
	 * @param key Key representing vertex, it will be used for comparisons with other
	 * vertices for equality. Should be unique in a graph.
	 * @param value Contains business data associated with a given graph vortex.
	 */
	public Vertex(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	
	
	/**
	 * @return Contains business data associated with a given graph vortex.
	 */
	public V getValue() {
		return this.value;
	}
	
	/**
	 * @param value Contains business data associated with a given graph vortex.
	 */
	public void setValue(V value) {
		this.value = value;
	}
	
	/**
	 * @return Key representing vertex, it will be used for comparisons with other
	 * vertices for equality. Should be unique in a graph.
	 */
	public K getKey() {
		return this.key;
	}

	/**
	 * @param key Key representing vertex, it will be used for comparisons with other
	 * vertices for equality. Should be unique in a graph.
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * @return String representation, just based on business data object.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.key).append('=').append(this.value);
		return sb.toString();
	}
}
