package edu.usun.common.graph;

import java.util.List;

/**
 * Graph representation. Contains vertices objects and edges (represented by
 * Edge objects and internally by adjacency matrix usually).
 * 
 * @author usun
 *
 * @param <K> Key representing a vertex, it will be used for comparisons with
 *            other vertices for equality. Should be unique in a graph. Cannot
 *            be null.
 * @param <V> Contains business data associated with a given graph vortex.
 *            Duplicates and null values are generally allowed, because K key
 *            template type is used to identify the vertex.
 */
public interface Graph<K, V> extends Cloneable {

	/**
	 * @return Indication if graph is empty (i.e. no vertices).
	 */
	boolean isEmpty();
	
	/**
	 * @return The size of the graph (amount of vertices).
	 */
	int size();

	/**
	 * Checks if vertex with a given key is a part of the graph.
	 * 
	 * @param key Key for vertex to check.
	 * @return True if key is not null and already added to the graph.
	 */
	boolean containsVertexKey(K key);

	/**
	 * Add vertex to the graph with only key information, business data is set to
	 * null.
	 * 
	 * @param key The key object representing the vertex.
	 * @return This updated graph object.
	 */
	Graph<K, V> addVertexKey(K key);

	/**
	 * Add vertex to the graph. Call containsVertexKey prior adding this unless you
	 * are sure there are no vertices with such key. Duplicates for keys are not
	 * allowed within a graph.
	 * 
	 * @param key   The key object representing the vertex.
	 * @param value The business data object associated with the vertex. Can be
	 *              null. Can have duplicates.
	 * @return This updated graph object.
	 */
	Graph<K, V> addVertex(K key, V value);
	
	/**
	 * Removes vertex with a given key from the graph, including 
	 * removal of all edges including it.
	 * 
	 * @param key The key of the vertex to remove. Throws IllegalArgumentException if key is not found.
	 * @return This updated graph object.
	 */
	Graph<K, V> removeVertex(K key);
	
	/**
	 * Link vertices with given keys. Single direction from key1 to key2. Unweighed.
	 * @param key1 The origin vertex to link. Should be part of the graph.
	 * @param key2 The destination vertex to link. Should be part of the graph.
	 * @return This updated graph object.
	 */
	Graph<K, V> addEdgeOneway(K key1, K key2);
	
	/**
	 * Link vertices with given keys. Bi-directional. Unweighed.
	 * @param key1 The first vertex to link. Symmetrical. Should be part of the graph.
	 * @param key2 The second vertex to link. Symmetrical. Should be part of the graph.
	 * @return This updated graph object.
	 */
	Graph<K, V> addEdge(K key1, K key2);
	
	/**
	 * Link vertices with given keys. Unweighed.
	 * @param key1 The first vertex to link. Should be part of the graph.
	 * @param key2 The second vertex to link. Should be part of the graph.
	 * @param biDirectional If the edge is bi-directional, if false the direction is from key1 to key2.
	 * @return This updated graph object.
	 */
	Graph<K, V> addEdge(K key1, K key2, boolean biDirectional);
	
	/**
	 * Unlink vertices with given keys. Bi-directional, meaning both directions are removed. Unweighed.
	 * @param key1 The first vertex to unlink. Symmetrical. Should be part of the graph.
	 * @param key2 The second vertex to unlink. Symmetrical. Should be part of the graph.
	 * @return This updated graph object.
	 */
	Graph<K, V> removeEdge(K key1, K key2);
	
	/**
	 * Unlink vertices with given keys.
	 * @param key1 The first vertex to unlink. Should be part of the graph.
	 * @param key2 The second vertex to unlink. Should be part of the graph.
	 * @param biDirectional If the edge is bi-directional, if false the direction is from key1 to key2.
	 * @return This updated graph object.
	 */
	Graph<K, V> removeEdge(K key1, K key2, boolean biDirectional);
	
	/**
	 * Checks if vertices with given keys are connected. Directions of edges are taken into account.
	 * @param key1 The source vertex key.
	 * @param key2 The destination vertex key.
	 * @return Indicates if there is a path from the given source vertex to destination.
	 */
	boolean areConnected(K source, K destination);
	
	/**
	 * @return Returns deep copy of the graph structure
	 * (K key object and V value object references are the same under vertices). 
	 */
	Graph<K, V> clone();
	
	/**
	 * Get vertex keys for directed acyclic graph (DAG) in the topologically sorted order.
	 * If cycles are found inside graph - an IllegalStateException is thrown on the runtime.
	 * @param graph The graph.
	 * @return List of the K vertex key objects of this graph in the topological order.
	 */
	List<K> getKeysInTopologicalOrder();
	
	/**
	 * @return The connectivity table. Each vertex is represent by the row 
	 * with a list of all connected (reachable) vertexes to it.
	 */
	List<List<K>> getConnectivityTable();
	
	/**
	 * Construct minimum spanning tree for this graph. The graph must be connected.
	 * @return A new sub-graph containing minimum spanning tree of the original graph.
	 */
	Graph<K, V> getMinSpanningTree();
	
	/**
	 * Finds a path from vertex represented by key1 to vertex represented by key2.
	 * @param key1 The origin vertex key.
	 * @param key2 The destination vertex key.
	 * @return The list of vertex keys representing a path from key1 to key2. The first one found is returned 
	 * depending on the underlying implementation.
	 */
	List<K> findPath(K key1, K key2);
	
	/**
	 * Finds the shortest path from vertex represented by key1 to vertex represented by key2.
	 * @param key1 The origin vertex key.
	 * @param key2 The destination vertex key.
	 * @return The list of vertex keys representing the shortest path from key1 to key2.
	 */
	List<K> findTheShortestPath(K key1, K key2);
}
