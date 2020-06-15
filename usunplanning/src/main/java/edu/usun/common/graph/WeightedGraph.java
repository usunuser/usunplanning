package edu.usun.common.graph;

/**
 * Weighted graph representation.
 * 
 * @author usun
 *
 * @param <K> Key representing a vertex, it will be used for comparisons with
 *            other vertices for equality. Should be unique in a graph. Cannot
 *            be null.
 * @param <V> Contains business data associated with a given graph vortex.
 *            Duplicates and null values are generally allowed, because K key
 *            template type is used to identify the vertex.
 * @param <W> Represents graph edge weight. Should be Comparable.
 */
public interface WeightedGraph<K, V, W extends Comparable<? super W>> extends Graph<K, V> {
	
	/**
	 * Link vertices specified under edge object. Edge weight is included.
	 * @param edge The edge object, contains origin and destination vertex keys, direction information, 
	 * and weight.
	 * @return This updated graph object.
	 */
	WeightedGraph<K, V, W> addEdge(Edge<K, W> edge);
	
}