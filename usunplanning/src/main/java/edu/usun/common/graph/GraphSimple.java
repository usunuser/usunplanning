package edu.usun.common.graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Graph simple implementation for unweighed. Not thread-safe.
 * Contains vertices objects. Edges are represented by adjacency matrix.
 * @author usun
 *
 * @param <K> Key representing a vertex, it will be used for comparisons with other
 * vertices for equality. Should be unique in a graph. Cannot be null.
 * @param <V> Contains business data associated with a given graph vortex. 
 * Duplicates and null values are generally allowed, because K key template type is used to identify the vertex.
 */
public class GraphSimple<K, V> implements Graph<K, V>, Cloneable {
	
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
	 * Default indication within adjacency matrix (unweighed) that there is no adjacency between vertices.
	 */
	protected static final int DEFAULT_ADJACENT = 1;

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
	public GraphSimple() {
		this(DEFAULT_MAX_SIZE);
	}
	
	/**
	 * Constructor with provided maximum expected amount of vertices. 
	 * If this amount is exceeded, internal graph storage will auto-expand.
	 * @param maxSize The maximum expected amount of vertices. Not a hard limit, 
	 * but it's advised to set it correctly for performance reasons. 
	 */
	public GraphSimple(int maxSize) {
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
	@Override
	public boolean isEmpty() {
		return this.vertices.isEmpty();
	}
	
	/**
	 * @return The size of the graph (amount of vertices).
	 */
	@Override
	public int size() {
		return this.vertices.size();
	}
	
	/**
	 * @param key Key for vertex to check.
	 * @return True if key is not null and already added to the graph.
	 */
	@Override
	public boolean containsVertexKey(K key) {
		return key != null && this.vertexLocation.containsKey(key);
	}
	
	/**
	 * Add vertex to the graph with only key information, business data is set to null.
	 * @param key The key object representing the vertex.
	 * @return This updated graph object.
	 */
	@Override
	public Graph<K, V> addVertexKey(K key) {
		return addVertex(key, null);
	}
	
	/**
	 * Add vertex to the graph. Call containsVertexKey prior adding this unless you are sure there are no 
	 * vertices with such key. Duplicates for keys are not allowed within a graph.
	 * @param key The key object representing the vertex.
	 * @param value The business data object associated with the vertex. Can be null. Can have duplicates.
	 * @return This updated graph object.
	 */
	@Override
	public Graph<K, V> addVertex(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException("Key representing vertex cannot be null.");
		}
		if (containsVertexKey(key)) {
			throw new IllegalArgumentException("Vertex with key " + key + " is already part of the graph.");
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
		return this;
	}
	
	/**
	 * Removes vertex with a given key from the graph, including 
	 * removal from adjacency matrix.
	 * 
	 * @param key The key of the vertex to remove. Throws IllegalArgumentException if key is not found.
	 * @return This updated graph object.
	 */
	@Override
	public Graph<K, V> removeVertex(K key) {
		if (key == null) {
			throw new IllegalArgumentException("Key representing vertex cannot be null.");
		}
		if (!containsVertexKey(key)) {
			throw new IllegalArgumentException("Vertex with key " + key + " is not part of the graph.");
		}
		
		int vertexIndex = this.vertexLocation.remove(key);
		this.vertices.remove(vertexIndex);
		// Shift all index locations for vertices after removed one.
		for (int i = vertexIndex; i < this.vertices.size(); i++) {
			Vertex<K, V> vertex = this.vertices.get(i);
			this.vertexLocation.put(vertex.getKey(), i);
		}
		
		this.adjacencyMatrix.remove(vertexIndex);
		this.adjacencyMatrix.stream().forEach(row -> row.remove(vertexIndex));
		
		return this;
	}
	
	/**
	 * Update adjacency matrix for provided vertices to mark them as linked. Single direction.
	 * @param key1 The first vertex to link. Should be part of the graph.
	 * @param key2 The second vertex to link. Should be part of the graph.
	 * @return This updated graph object.
	 */
	@Override
	public Graph<K, V> addEdgeOneway(K key1, K key2) {
		return addEdge(key1, key2, false);
	}
	
	/**
	 * Update adjacency matrix for provided vertices to mark them as linked. Bi-directional.
	 * @param key1 The first vertex to link. Symmetrical. Should be part of the graph.
	 * @param key2 The second vertex to link. Symmetrical. Should be part of the graph.
	 * @return This updated graph object.
	 */
	@Override
	public Graph<K, V> addEdge(K key1, K key2) {
		return addEdge(key1, key2, true);
	}
	
	/**
	 * Update adjacency matrix for provided vertices to mark them as linked.
	 * @param key1 The first vertex to link. Should be part of the graph.
	 * @param key2 The second vertex to link. Should be part of the graph.
	 * @param biDirectional If the edge is bi-directional.
	 * @return This updated graph object.
	 */
	@Override
	public Graph<K, V> addEdge(K key1, K key2, boolean biDirectional) {
		//System.out.println("New edge: " + key1 + (biDirectional ? "<->" : "->") + key2);
		updateAdjacency(key1, key2, DEFAULT_ADJACENT, biDirectional);
		return this;
	}
	
	/**
	 * Update adjacency matrix for provided vertices to mark them as not linked. Bi-directional.
	 * @param key1 The first vertex to unlink. Symmetrical. Should be part of the graph.
	 * @param key2 The second vertex to unlink. Symmetrical. Should be part of the graph.
	 * @return This updated graph object.
	 */
	@Override
	public Graph<K, V> removeEdge(K key1, K key2) {
		return removeEdge(key1, key2, true);
	}
	
	/**
	 * Update adjacency matrix for provided vertices to mark them as not linked.
	 * @param key1 The first vertex to unlink. Should be part of the graph.
	 * @param key2 The second vertex to unlink. Should be part of the graph.
	 * @param biDirectional If the edge is bi-directional, if false the direction is from key1 to key2.
	 * @return This updated graph object.
	 */
	@Override
	public Graph<K, V> removeEdge(K key1, K key2, boolean biDirectional) {
		//System.out.println("Remove edge: " + key1 + (biDirectional ? "<->" : "->") + key2);
		updateAdjacency(key1, key2, NOT_ADJACENT, biDirectional);
		return this;
	}
	
	/**
	 * Checks if vertices with given keys are connected. Directions of edges are taken into account.
	 * @param key1 The source vertex key.
	 * @param key2 The destination vertex key.
	 * @return Indicates if there is a path from the given source vertex to destination.
	 */
	@Override
	public boolean areConnected(K source, K destination) {
		if (!containsVertexKey(source)) {
			throw new IllegalArgumentException("Vertex with key " + source + " is not part of the graph.");
		}
		if (!containsVertexKey(destination)) {
			throw new IllegalArgumentException("Vertex with key " + destination + " is not part of the graph.");
		}
		
		// Even faster solution requires O(N^2) extra space for Integer references to hold transitive closure table.
		// Warshall's algorithm used
		List<List<Integer>> transitiveClosure = getTransitiveClosureTable();
		int indexKeySrc = this.vertexLocation.get(source);
		int indexKeyDest = this.vertexLocation.get(destination);
		int adjacency = transitiveClosure.get(indexKeySrc).get(indexKeyDest);
		return NOT_ADJACENT != adjacency;
	}
	
	/**
	 * Get vertex keys for directed acyclic graph (DAG) in the topologically sorted order.
	 * If cycles are found inside graph - an IllegalStateException is thrown on the runtime.
	 * @return List of the K vertex key objects of this graph in the topological order.
	 */
	public List<K> getKeysInTopologicalOrder() {
		LinkedList<K> sortedList = new LinkedList<K>();
		
		// Find vertex without successor, remove it, put it as a next element from the end 
		// of the sorted list, repeat.
		
		// Work with a graph copy, since we are going to remove vertices one by one.
		GraphSimple<K, V> graphCopy = (GraphSimple<K, V>) this.clone();
		int vertexIndex;
		while ((vertexIndex = graphCopy.getFirstNoSuccessorVertex()) >= 0) {
			K vertexKey = graphCopy.vertices.get(vertexIndex).getKey();
			
			// Remove this vertex from the graph copy.
			graphCopy.removeVertex(vertexKey);
			
			// Add in reverse order to the final sorted result list.
			sortedList.push(vertexKey);
		}
		
		if (sortedList.size() < this.size()) {
			// Failed to find no-successor vertex, meaning the graph is not a directed acyclic graph
			throw new IllegalStateException("This graph is not a directed acyclic graph, a cycle was found.");
		}
		return sortedList;
	}
	
	/**
	 * @return The transitive closure table built off adjacency matrix using Warshall's algorithm.
	 */
	protected List<List<Integer>> getTransitiveClosureTable() {
		List<List<Integer>> transitiveClosure = duplicate(this.adjacencyMatrix);
		for (int rowIndex = 0; rowIndex < transitiveClosure.size(); rowIndex++) {
			List<Integer> row = transitiveClosure.get(rowIndex);
			for (int colIndex = 0; colIndex < row.size(); colIndex++) {
				int adjacency = row.get(colIndex);
				if (NOT_ADJACENT != adjacency) {
					for (int z = 0; z < transitiveClosure.size(); z++) {
						int secondaryAdjacency = transitiveClosure.get(z).get(rowIndex);
						if (NOT_ADJACENT != secondaryAdjacency) {
							// Secondary adjacency found, mark remote vertices as directly adjacent.
							// Sum up weighs.
							transitiveClosure.get(z).set(colIndex, adjacency + secondaryAdjacency);
						}
					}
				}
			}
		}
		return transitiveClosure;
	}
	
	protected List<List<Integer>> duplicate(List<List<Integer>> original) {
		List<List<Integer>> result = new ArrayList<List<Integer>>(original.size());
		for (List<Integer> srcList : original) {
			List<Integer> destList = new ArrayList<Integer>(srcList.size());
			result.add(destList);
			for (Integer value : srcList) {
				destList.add(value);
			}
		}
		return result;
	}
	
	
	/**
	 * Finds a path from vertex represented by key1 to vertex represented by key2.
	 * @param key1 The origin vertex key.
	 * @param key2 The destination vertex key.
	 * @return The list of vertex keys representing a path from key1 to key2. The first one found is returned 
	 * depending on the underlying implementation.
	 */
	@Override
	public List<K> findPath(K key1, K key2) {
		// Depth-first search finds any path faster than breadth-first search.
		return getConnectivityPathThroughDepthFirstSearch(key1, key2);
	}
	
	/**
	 * Finds the shortest path from vertex represented by key1 to vertex represented by key2.
	 * @param key1 The origin vertex key.
	 * @param key2 The destination vertex key.
	 * @return The list of vertex keys representing the shortest path from key1 to key2.
	 */
	@Override
	public List<K> findTheShortestPath(K key1, K key2) {
		// Breadth-first search finds the shortest path between ket1 and key2.
		return getConnectivityPathThroughBreadthFirstSearch(key1, key2);
	}
	
	/**
	 * @return The connectivity table. Each vertex is represent by the row 
	 * with a list of all connected (reachable) vertexes to it.
	 */
	@Override
	public List<List<K>> getConnectivityTable() {
		List<List<K>> connectivityTable = new ArrayList<List<K>>(this.vertices.size());
		for (Vertex<K, V> vertex : vertices) {
			connectivityTable.add(getAllConnectedKeysThroughDepthFirstSearch(vertex.getKey()));
		}
		return connectivityTable;
	}
	
	protected List<K> getAllConnectedKeysThroughDepthFirstSearch(K key) {
		if (!containsVertexKey(key)) {
			throw new IllegalArgumentException("Vertex with key " + key + " is not part of the graph.");
		}
		
		// Start looking.
		Deque<Integer> vertexIndexesStack = new LinkedList<Integer>();
		// Track all visited vertices to reset them in the end.
		// Also will be used to build list of keys based on these indexes
		List<Integer> visitedIndexes = new LinkedList<Integer>();
		int vertexIndex = getVertexIndex(key);
		visitVertex(vertexIndex);
		visitedIndexes.add(vertexIndex);
		vertexIndexesStack.push(vertexIndex);
		
		// Recursive search.
		while (!vertexIndexesStack.isEmpty()) {
			vertexIndex = vertexIndexesStack.peek();
			
			// Next unvisited adjacent vertex (to push deeper).
			int nextVertexIndex = getNextNotVisitedAdjacentVertex(vertexIndex);
			if (nextVertexIndex < 0) {
				// Deadend.
				vertexIndexesStack.pop();
			} else {
				// Mark as visited and push on stack.
				visitVertex(nextVertexIndex);
				vertexIndexesStack.push(nextVertexIndex);
				visitedIndexes.add(nextVertexIndex);
			}
		}
		
		// Reset all visited vertices and collect result keys.
		List<K> visitedKeys = new ArrayList<K>(visitedIndexes.size());
		for (int visitedIndex : visitedIndexes) {
			unvisitVertex(visitedIndex);
			visitedKeys.add(this.vertices.get(visitedIndex).getKey());
		}
		return visitedKeys;
	}
	
	protected List<K> getConnectivityPathThroughDepthFirstSearch(K key1, K key2) {
		if (!containsVertexKey(key1)) {
			throw new IllegalArgumentException("Vertex with key " + key1 + " is not part of the graph.");
		}
		if (!containsVertexKey(key2)) {
			throw new IllegalArgumentException("Vertex with key " + key2 + " is not part of the graph.");
		}
		
		// Start looking in reverse from Key2 (depth-first search, DFS) to reuse a stack as a found path.
		Deque<Integer> vertexIndexesStack = new LinkedList<Integer>();
		// Track all visited vertices to reset them in the end. 
		List<Integer> visitedIndexes = new LinkedList<Integer>();
		int vertexIndex = getVertexIndex(key2);
		visitVertex(vertexIndex);
		visitedIndexes.add(vertexIndex);
		vertexIndexesStack.push(vertexIndex);
		
		// Recursive search.
		while (!vertexIndexesStack.isEmpty()) {
			vertexIndex = vertexIndexesStack.peek();
			
			// Next unvisited adjacent vertex (to push deeper).
			int nextVertexIndex = getNextNotVisitedAdjacentVertex(vertexIndex);
			if (nextVertexIndex < 0) {
				// Deadend.
				vertexIndexesStack.pop();
			} else {
				// Mark as visited and push on stack.
				visitVertex(nextVertexIndex);
				vertexIndexesStack.push(nextVertexIndex);
				visitedIndexes.add(nextVertexIndex);
				
				// Doing it as a second block in "else" to have the full path recorded on the stack
				K nextKey = this.vertices.get(nextVertexIndex).getKey();
				if (key1.equals(nextKey)) {
					break;
				}
			}
		}
		
		// Reset all visited vertices.
		visitedIndexes.stream().forEach(index -> unvisitVertex(index));
		
		// Convert vertex indexes to vertex key objects
		return vertexIndexesStack.stream()
			.map(index -> this.vertices.get(index).getKey())
			.collect(Collectors.toList());
	}
	
	protected List<K> getConnectivityPathThroughBreadthFirstSearch(K key1, K key2) {
		if (!containsVertexKey(key1)) {
			throw new IllegalArgumentException("Vertex with key " + key1 + " is not part of the graph.");
		}
		if (!containsVertexKey(key2)) {
			throw new IllegalArgumentException("Vertex with key " + key2 + " is not part of the graph.");
		}
		
		Deque<Integer> shortestPath = new LinkedList<Integer>();
		
		// Start looking from Key1 (depth-first search, DFS).
		Deque<Integer> vertexIndexesQueue = new LinkedList<Integer>();
		// Track all visited vertices to reset them in the end. 
		List<Integer> visitedIndexes = new LinkedList<Integer>();
		int vertexIndex = getVertexIndex(key1);
		visitVertex(vertexIndex);
		visitedIndexes.add(vertexIndex);
		vertexIndexesQueue.add(vertexIndex);
		
		// Recursive wide search. Will find the shortest path.
	outer:
		while (!vertexIndexesQueue.isEmpty()) {
			vertexIndex = vertexIndexesQueue.remove();
			
			int nextVertexIndex;
			// Go through all adjacent not visited yet vertices. 
			while ((nextVertexIndex = getNextNotVisitedAdjacentVertex(vertexIndex)) >= 0) {
				K nextKey = this.vertices.get(nextVertexIndex).getKey();
				if (key2.equals(nextKey)) {
					/*
					 * Search shortest A-F 
					 * 
					 *     B+
					 *   /
					 * A+- C+ - E+ - G
					 *   \        /
					 *     D+ - F
					 *     
					 * + marks visited, so we need to go backwards through adjusted visited vertices.
					 */
					shortestPath.push(nextVertexIndex);
					int previousVertexIndex = nextVertexIndex;
					while ((previousVertexIndex = getNextVisitedAdjacentVertex(previousVertexIndex)) >= 0) {
						shortestPath.push(previousVertexIndex);
						unvisitVertex(previousVertexIndex);
						K previousKey = this.vertices.get(previousVertexIndex).getKey();
						if (key1.equals(previousKey)) {
							break;
						}
					}
					break outer;
				}
				
				visitVertex(nextVertexIndex);
				visitedIndexes.add(nextVertexIndex);
				vertexIndexesQueue.add(nextVertexIndex);
			}
		}
		
		// Reset all visited vertices.
		visitedIndexes.stream().forEach(index -> unvisitVertex(index));
		
		// Convert vertex indexes to vertex key objects
		return shortestPath.stream()
			.map(index -> this.vertices.get(index).getKey())
			.collect(Collectors.toList());
	}
	
	/**
	 * Construct minimum spanning tree for this graph.
	 * @return A new sub-graph containing minimum spanning tree of the original graph.
	 */
	@Override
	public Graph<K, V> getMinSpanningTree() {
		return getMinSpanningTreeThroughBreadthFirstSearch();
	}
	
	protected Graph<K, V> getMinSpanningTreeThroughBreadthFirstSearch() {
		GraphSimple<K, V> mst = new GraphSimple<K, V>(this.initialMaxSize);
		if (isEmpty()) {
			return mst;
		}
		// Start looking from Key1 (depth-first search, DFS).
		Deque<Integer> vertexIndexesQueue = new LinkedList<Integer>();
		// Track all visited vertices to reset them in the end. 
		List<Integer> visitedIndexes = new LinkedList<Integer>();
		
		int vertexIndex = 0;
		visitVertex(vertexIndex);
		visitedIndexes.add(vertexIndex);
		vertexIndexesQueue.add(vertexIndex);
		
		// Adding entry vertex to MST.
		Vertex<K, V> vertex = this.vertices.get(vertexIndex);
		mst.addVertex(vertex.getKey(), vertex.getValue());
		int previousVertexIndex;
		
		// Recursive wide search.
		while (!vertexIndexesQueue.isEmpty()) {
			vertexIndex = vertexIndexesQueue.remove();
			previousVertexIndex = vertexIndex;
			
			int nextVertexIndex;
			// Go through all adjacent not visited yet vertices. 
			while ((nextVertexIndex = getNextNotVisitedAdjacentVertex(vertexIndex)) >= 0) {
				visitVertex(nextVertexIndex);
				visitedIndexes.add(nextVertexIndex);
				vertexIndexesQueue.add(nextVertexIndex);
				
				// Adding entry vertex to MST.
				vertex = this.vertices.get(nextVertexIndex);
				mst.addVertex(vertex.getKey(), vertex.getValue());
				// Add edge to previous vertex on this path.
				mst.addEdge(this.vertices.get(previousVertexIndex).getKey(), vertex.getKey());
			}
		}
		
		// Reset all visited vertices.
		visitedIndexes.stream().forEach(index -> unvisitVertex(index));
		
		// Convert visited 
		return mst;
	}
	
	protected void visitVertex(int vertexIndex) {
		//System.out.println("visit " + vertexIndex + ", key=" + this.vertices.get(vertexIndex).getKey());
		this.vertices.get(vertexIndex).visit();
	}
	
	protected void unvisitVertex(int vertexIndex) {
		//System.out.println("unvisit " + vertexIndex + ", key=" + this.vertices.get(vertexIndex).getKey());
		this.vertices.get(vertexIndex).unvisit();
	}
	
	protected int getNextNotVisitedAdjacentVertex(int vertexIndex) {
		 return getNextAdjacentVertex(vertexIndex, false);
	}
	
	protected int getNextVisitedAdjacentVertex(int vertexIndex) {
		 return getNextAdjacentVertex(vertexIndex, true);
	}
	
	protected int getNextAdjacentVertex(int vertexIndex, boolean visited) {
		List<Integer> adjacencyStates = this.adjacencyMatrix.get(vertexIndex);
		for (int index = 0; index < adjacencyStates.size(); index++) {
			if (NOT_ADJACENT == adjacencyStates.get(index)) {
				continue;
			}
			Vertex<K, V> vertex = this.vertices.get(index);
			// negation of XOR
			// visited = false, vertex.isVisited=false -> XOR = false -> !XOR = true
			// visited = true, vertex.isVisited=true -> XOR = false -> !XOR = true
			if (!(visited ^ vertex.isVisited())) {
				return index;
			}
		}
		// Nothing found
		return -1;
	}
	
	/**
	 * Update value in adjacency matrix for provided vertices.
	 * @param key1 The first vertex to link. Symmetrical. Should be part of the graph.
	 * @param key2 The second vertex to link. Symmetrical. Should be part of the graph.
	 * @param adjacencyValue New adjacency value for the matrix.
	 * @param biDirectional If the edge is bi-directional.
	 */
	protected void updateAdjacency(K key1, K key2, int adjacencyValue, boolean biDirectional) {
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
		if (biDirectional) {
			this.adjacencyMatrix.get(index2).set(index1, adjacencyValue);
		}
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
	 * @return First found vertex which doesn't lead (adjacent) to any 
	 * other vertex. -1 if not found (e.g. graph is cyclic).
	 */
	protected int getFirstNoSuccessorVertex() {
		/*
		 * A,B,C,D,E,F,G,H,I
		 * 0,1,0,0,0,0,0,0,0 A
		 * 0,0,1,0,0,0,0,0,0 B
		 * 0,0,0,0,0,0,1,0,0 C
		 * 0,0,0,0,0,0,1,0,0 D
		 * 0,0,0,0,0,1,0,0,0 E
		 * 0,0,0,0,0,0,0,1,0 F
		 * 0,0,0,0,0,0,0,0,1 G
		 * 0,0,0,0,0,0,0,0,1 H
		 * 0,0,0,0,0,0,0,0,0 I
		 * 
		 * "I" will be the first with no successors.
		 */
		for (int rowIndex = 0; rowIndex < this.adjacencyMatrix.size(); rowIndex++) {
			List<Integer> row = this.adjacencyMatrix.get(rowIndex);
			boolean hasSuccessors = false;
			for (Integer adjacency : row) {
				if (adjacency != null && 
					NOT_ADJACENT != adjacency.intValue()) {
					// Has a successor vertex, skip the whole row/vertex.
					hasSuccessors = true;
					break;
				}
			}
			// If flag was not set to true, no successors found and this row index 
			// is the index of the vertex without successors.
			if (!hasSuccessors) {
				return rowIndex;
			}
		}
		
		return -1;
	}
	
	/**
	 * @return Returns deep copy of the graph structure
	 * (K key object and V value object references are the same under vertices). 
	 */
	@Override
	public Graph<K, V> clone() {
		try {
			@SuppressWarnings("unchecked")
			// Copy primitives and references.
			GraphSimple<K, V> copy = (GraphSimple<K, V>) super.clone();
			
			// Reset collections to recreate them.
			int newMaxSize = newAdjacencyMatrixMaximumDimension(this.vertices.size());
			copy.vertices = new ArrayList<Vertex<K, V>>(newMaxSize);
			copy.adjacencyMatrix = new ArrayList<List<Integer>>(newMaxSize);
			copy.vertexLocation = new HashMap<K, Integer>(newMaxSize * 2, 0.5f);
			
			// Add vertices without edges.
			for (Vertex<K, V> vertex : this.vertices) {
				copy.addVertex(vertex.getKey(), vertex.getValue());
			}
			// Copy edges / adjacency maxtrix.
			for (int rowIndex = 0; rowIndex < this.adjacencyMatrix.size(); rowIndex++) {
				List<Integer> srcList = this.adjacencyMatrix.get(rowIndex);
				List<Integer> destList = copy.adjacencyMatrix.get(rowIndex);
				Collections.copy(destList, srcList);
			}
			return copy;
		} catch (CloneNotSupportedException e) {
			throw new InternalError("Incorrect implementation of edu.usun.common.graph.Graph#clone()", e);
		}
	}
	
	
	/**
	 * Trace logging into System.out, to be used only for local development for debugging purposes.
	 */
	protected void traceGraph() {
		System.out.println("Adjacency matrix:");
		System.out.println(this.vertices.stream()
			.map(vertex -> vertex == null || vertex.getKey() == null ? "null" : vertex.getKey().toString())
			.collect(Collectors.joining(",")));
		for (int rowIndex = 0; rowIndex < this.adjacencyMatrix.size(); rowIndex++) {
			System.out.print(this.adjacencyMatrix.get(rowIndex).stream()
				.map(adjacency -> adjacency.toString())
				.collect(Collectors.joining(",")));
			System.out.println(" " + this.vertices.get(rowIndex).getKey());
		}
		
		System.out.println("Vertex keys to indexes: " + this.vertexLocation);
	}
	
	/**
	 * Local tests only during initial development. Use unit tests for actual autotesting.
	 * @param args System arguments.
	 */
	public static void main(String[] args) {
		System.out.println("----------------");
		GraphSimple<String, String> graph =  new GraphSimple<String, String>(5);
		graph.addVertexKey("A");
		graph.addVertexKey("B");
		graph.addVertexKey("C");
		graph.addVertexKey("D");
		graph.addVertexKey("E");
		graph.addEdge("A", "C");
		graph.addEdge("A", "D");
		graph.addEdge("B", "C");
		graph.addEdge("B", "E");
		System.out.println("Added: A,B,C,D,E; linked: AC,AD,BC,BE");
		graph.traceGraph();
		System.out.println("----------------");
		
		
		graph.addVertexKey("F");
		graph.addVertexKey("G");
		graph.addEdge("E", "F");
		graph.addEdge("F", "G");
		graph.addEdge("A", "E");
		graph.addEdge("A", "G");
		System.out.println("Added: F,G; linked: EF,FG,AE,AG");
		graph.traceGraph();
		System.out.println("----------------");
		
		graph.removeEdge("F", "G");
		System.out.println("Unlinked: FG");
		graph.traceGraph();
		System.out.println("----------------");
		
		System.out.println("Are A and E connected (DFS)?: " + graph.areConnected("A", "E"));
		System.out.println("A->E (DFS): " + graph.findPath("A", "E"));
		System.out.println("A->E (BFS): " + graph.findTheShortestPath("A", "E"));
		System.out.println("F->A (DFS): " + graph.findPath("F", "A"));
		System.out.println("F->A (BFS): " + graph.findTheShortestPath("F", "A"));
		System.out.println("----------------");
		
		System.out.println("Original Graph:");
		/*
		 * Original: AC, AD, BC, BE, EF, FG, AE, AG
		 * 
		 *      G
		 *   D / \
		 *   |/   \ 
		 *   A-C-B |
		 *   |  /  |
		 *   | /   |
		 *   |/   / 
		 *   E-F--
		 */
		graph.traceGraph();
		/* MST: AC, AD, AE, AG, CB, EF
		 *
		 *      G
		 *   D /
		 *   |/
		 *   A-C-B
		 *   |
		 *   E-F
		 */
		System.out.println("MST:");
		((GraphSimple<String, String>) graph.getMinSpanningTree()).traceGraph();
		System.out.println("----------------");
		
		
		graph =  new GraphSimple<String, String>(5);
		/*
		 * A -> B -> C
		 *            \
		 *             -> G
		 *            /    \
		 *           D      -> I
		 *                 /
		 *      E -> F -> H
		 *      
		 * AB,BC,CG,DG,EF,FH,GI,HI
		 * 
		 */
		graph.addVertexKey("A").addVertexKey("B").addVertexKey("C").addVertexKey("D").addVertexKey("E")
			.addVertexKey("F").addVertexKey("G").addVertexKey("H").addVertexKey("I");
		graph.addEdgeOneway("A", "B").addEdgeOneway("B", "C").addEdgeOneway("C", "G").addEdgeOneway("D", "G")
			.addEdgeOneway("E", "F").addEdgeOneway("F", "H").addEdgeOneway("G", "I").addEdgeOneway("H", "I");
		graph.traceGraph();
		System.out.println("Expected vertex without successors is I, index = " +
			graph.getVertexIndex("I") + ", index found: " + graph.getFirstNoSuccessorVertex());
		System.out.println("----------------");
		
		System.out.println("Deep copy of the graph:");
		GraphSimple<String, String> graphCopy = (GraphSimple<String, String>) graph.clone();
		graphCopy.traceGraph();
		System.out.println("Removed vertex G from the copy:");
		graphCopy.removeVertex("G");
		graphCopy.traceGraph();
		System.out.println("----------------");
		
		System.out.println("Key in topological order: " + graph.getKeysInTopologicalOrder());
		System.out.println("----------------");
		System.out.println("Original graph:");
		graph.traceGraph();
		System.out.println("----------------");
		
		System.out.println("Connectivity table:");
		System.out.println(graph.getConnectivityTable());
		System.out.println("----------------");
		

		System.out.println("Transitive closure table:");
		List<List<Integer>> transitiveClosureTable = graph.getTransitiveClosureTable();
		System.out.println(graph.vertices.stream()
				.map(vertex -> vertex == null || vertex.getKey() == null ? "null" : vertex.getKey().toString())
				.collect(Collectors.joining(",")));
		for (int rowIndex = 0; rowIndex < transitiveClosureTable.size(); rowIndex++) {
			System.out.print(transitiveClosureTable.get(rowIndex).stream()
				.map(adjacency -> adjacency.toString())
				.collect(Collectors.joining(",")));
			System.out.println(" " + graph.vertices.get(rowIndex).getKey());
		}
		System.out.println();
		System.out.println("----------------");
		
		/*
		 * A -> B -> C
		 *            \
		 *             -> G
		 *            /    \
		 *           D      -> I
		 *                 /
		 *      E -> F -> H
		 *      
		 * AB,BC,CG,DG,EF,FH,GI,HI
		 * 
		 */
		System.out.println("Connectivity check:");
		System.out.println("Can you travel from D to I? should be true: " + graph.areConnected("D", "I"));
		System.out.println("Can you travel from I to D? should be false: " + graph.areConnected("I", "D"));
		System.out.println("----------------");
		
	}
}
