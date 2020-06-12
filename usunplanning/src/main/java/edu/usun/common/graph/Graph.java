package edu.usun.common.graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Graph representation. Not thread-safe.
 * Contains vertices objects. Edges are represented by adjacency matrix.
 * @author usun
 *
 * @param <K> Key representing a vertex, it will be used for comparisons with other
 * vertices for equality. Should be unique in a graph. Cannot be null.
 * @param <V> Contains business data associated with a given graph vortex. 
 * Duplicates and null values are generally allowed, because K key template type is used to identify the vertex.
 */
public class Graph <K, V> {
	
	/**
	 * Maximum amount of vertices allowed is 23170, which is around a half of 
	 * square root of maximum integer and minus 8. The limitation is due to adjacency 
	 * maxtrix which will be created which is N*N size in 2d array holding Integer values and 
	 * multiple internal collections.
	 */
	public static final int MAXIMMUM_VERTICES_SIZE = 23170;
	
	/**
	 * Default size for expected maximum amount of vertices.
	 */
	protected static final int DEFAULT_MAX_SIZE = 8;
	
	/**
	 * Indication within adjacency matrix that there is no adjacency between vertices.
	 */
	protected static final int NOT_ADJACENT = 0;
	
	/**
	 * Indication within adjacency matrix that there is no adjacency between vertices.
	 */
	protected static final int ADJACENT = 1;

	/**
	 * Auto-expandable list holding vertices of the graph.
	 */
	protected List<Vertex<K, V>> vertices;
	
	/**
	 * To ensure uniqueness of the keys used for graph vertices, 
	 * it is also used to track it's index to find in internal lists 
	 * and adjacency matrix.
	 */
	protected Map<K, Integer> vertexLocation;
	
	/**
	 * Initial max size allocated. 
	 * Max of it and current size will be used internally to 
	 * account for auto-extensions of graph size.
	 */
	protected int initialMaxSize;
	
	/**
	 * Adjacency graph matrix.
	 */
	protected List<List<Integer>> adjacencyMatrix;
	
	/**
	 * Default constructor with default expected maximum amount of vertices.
	 * If exceeded, the graph will auto-expand.
	 */
	public Graph() {
		this(DEFAULT_MAX_SIZE);
	}
	
	/**
	 * Constructor with provided maximum expected amount of vertices. 
	 * If this amount is exceeded, internal graph storage will auto-expand.
	 * @param maxSize The maximum expected amount of vertices. Not a hard limit, 
	 * but it's advised to set it correctly for performance reasons. 
	 */
	public Graph(int maxSize) {
		super();
		if (maxSize <= 0 || maxSize > MAXIMMUM_VERTICES_SIZE) {
			throw new IllegalArgumentException(
				"Maximum expected amount of vertices should be >0 and <=" + MAXIMMUM_VERTICES_SIZE);
		}
		this.initialMaxSize = maxSize;
		this.vertices = new ArrayList<Vertex<K, V>>(maxSize);
		this.adjacencyMatrix = new ArrayList<List<Integer>>(maxSize);
		this.vertexLocation = new HashMap<K, Integer>(maxSize * 2, 0.5f);
	}
	
	/**
	 * @return Indication if graph is empty (i.e. no vertices).
	 */
	public boolean isEmpty() {
		return this.vertices.isEmpty();
	}
	
	/**
	 * @param key Key for vertex to check.
	 * @return True if key is not null and already added to the graph.
	 */
	public boolean containsVertexKey(K key) {
		return key != null && this.vertexLocation.containsKey(key);
	}
	
	/**
	 * Add vertex to the graph with only key information, business data is set to null.
	 * @param key The key object representing the vertex.
	 */
	public void addVertexKey(K key) {
		addVertex(key, null);
	}
	
	/**
	 * Add vertex to the graph. Call containsVertexKey prior adding this unless you are sure there are no 
	 * vertices with such key. Duplicates for keys are not allowed within a graph.
	 * @param key The key object representing the vertex.
	 * @param value The business data object associated with the vertex. Can be null. Can have duplicates.
	 */
	public void addVertex(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException("Key representing vertex cannot be null.");
		}
		if (containsVertexKey(key)) {
			throw new IllegalArgumentException("Vertex key " + key + "is already part of the graph.");
		}
		int newVertexIndex = this.vertices.size();
		// Auto-expandable
		this.vertices.add(new Vertex<K,V>(key, value));
		this.vertexLocation.put(key, newVertexIndex);
		
		// Append new node into adjacency matrix both like new column and a row, 
		// it is added as a disconnected vertex, so all values are 0.
		for (List<Integer> column : this.adjacencyMatrix) {
			column.add(NOT_ADJACENT);
		}
		// Populating new row without adjacency.
		List<Integer> row = new ArrayList<Integer>(
			newAdjacencyMatrixMaximumDimension(newVertexIndex + 1));
		for (int i = 0; i <= newVertexIndex; i++) {
			row.add(NOT_ADJACENT);
		}
		// Add new row.
		this.adjacencyMatrix.add(row);
	}
	
	/**
	 * Update adjacency matrix for provided vertices to mark them as linked.
	 * @param key1 The first vertex to link. Symmetrical. Should be part of the graph.
	 * @param key2 The second vertex to link. Symmetrical. Should be part of the graph.
	 */
	public void link(K key1, K key2) {
		updateAdjacency(key1, key2, ADJACENT);
	}
	
	/**
	 * Update adjacency matrix for provided vertices to mark them as not linked.
	 * @param key1 The first vertex to unlink. Symmetrical. Should be part of the graph.
	 * @param key2 The second vertex to unlink. Symmetrical. Should be part of the graph.
	 */
	public void unlink(K key1, K key2) {
		updateAdjacency(key1, key2, NOT_ADJACENT);
	}
	
	/**
	 * Update value in adjacency matrix for provided vertices.
	 * @param key1 The first vertex to link. Symmetrical. Should be part of the graph.
	 * @param key2 The second vertex to link. Symmetrical. Should be part of the graph.
	 * @param adjacencyValue New adjacency value for the matrix.
	 */
	protected void updateAdjacency(K key1, K key2, int adjacencyValue) {
		if (!containsVertexKey(key1)) {
			throw new IllegalArgumentException("Vertex with key " + key1 + " is not part of the graph.");
		}
		if (!containsVertexKey(key2)) {
			throw new IllegalArgumentException("Vertex with key " + key2 + " is not part of the graph.");
		}
		int index1 = getVertexIndex(key1);
		int index2 = getVertexIndex(key2);
		
		// Populate for both places in the mirror matrix.
		this.adjacencyMatrix.get(index1).set(index2, adjacencyValue);
		this.adjacencyMatrix.get(index2).set(index1, adjacencyValue);
	}
	
	/**
	 * @param key Vertex key to search for.
	 * @return Vertex index for internal storages (vertices list, adjacency matrix).
	 */
	protected int getVertexIndex(K key) {
		if (!this.vertexLocation.containsKey(key)) {
			throw new IllegalArgumentException("Vertex with key " + key + " is not part of the graph.");
		}
		return this.vertexLocation.get(key);
	}
	
	/**
	 * @param currentSize Current size (in terms of elements there of assessed collections).
	 * @return The maximum of currentSize and initial max size allocated to the graph.
	 */
	protected int newAdjacencyMatrixMaximumDimension(int currentSize) {
		return Math.max(currentSize, this.initialMaxSize);
	}
	
	/**
	 * Trace logging into System.out, to be used only for local development for debugging purposes.
	 */
	public void traceGraph() {
		System.out.println("Vertice keys: ");
		System.out.println(this.vertices.stream()
			.map(vertex -> vertex == null || vertex.getKey() == null ? "null" : vertex.getKey().toString())
			.collect(Collectors.joining(",")));
		System.out.println("Adjacency matrix:");
		this.adjacencyMatrix.stream()
			.forEachOrdered(list -> System.out.println(
				list.stream()
					.map(adj -> adj.toString())
					.collect(Collectors.joining(","))));
		System.out.println("Vertex keys to indexes: " + this.vertexLocation);
	}
	
	/**
	 * Local tests only during initial development. Use unit tests for actual autotesting.
	 * @param args System arguments.
	 */
	public static void main(String[] args) {
		System.out.println("----------------");
		Graph<String, String> graph =  new Graph<String, String>(5);
		graph.addVertexKey("A");
		graph.addVertexKey("B");
		graph.addVertexKey("C");
		graph.addVertexKey("D");
		graph.addVertexKey("E");
		graph.link("A", "C");
		graph.link("A", "D");
		graph.link("B", "C");
		graph.link("B", "E");
		System.out.println("Added: A,B,C,D,E; linked: AC,AD,BC,BE");
		graph.traceGraph();
		System.out.println("----------------");
		
		
		graph.addVertexKey("F");
		graph.addVertexKey("G");
		graph.link("E", "F");
		graph.link("F", "G");
		graph.link("A", "G");
		System.out.println("Added: F,G; linked: EF,FG,AG");
		graph.traceGraph();
		System.out.println("----------------");
		
		graph.unlink("F", "G");
		System.out.println("Unlinked: FG");
		graph.traceGraph();
		System.out.println("----------------");
		
	}
}
