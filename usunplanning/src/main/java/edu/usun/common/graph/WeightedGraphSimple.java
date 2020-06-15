package edu.usun.common.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.usun.common.util.HeapArray;

/**
 * Weighted graph default simple implementation. Not thread-safe.
 * Edge weight is nominated in integer values in this simple implementation. 
 * 0 weight means that vertices are not connected by such edge.
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
public class WeightedGraphSimple<K, V> extends GraphSimple<K, V> implements WeightedGraph<K, V, WeightedGraphSimple.EdgeWeight> {

	/**
	 * Edge weight, simple implementation with integer weight value to 
	 * hold it directly in adjacency matrix of the graph. Immutable.
	 * @author usun
	 */
	protected static class EdgeWeight implements Comparable<EdgeWeight> {

		/** 
		 * Simple integer edge weight.
		 */
		private int weight;
		
		/**
		 * 
		 * @param weight
		 */
		public EdgeWeight(int weight) {
			super();
			this.weight = weight;
		}
		
		/**
		 * @return Simple integer edge weight.
		 */
		public int getWeight() {
			return weight;
		}

		/**
		 * Reverse comparison to search for min everywhere, not maximum.
		 * @param o Weight to compare to.
		 * @return 0 if weights are equal; negative if this object weight is larger than object weight to compare to;
		 * positive if this object weight is smaller than object weight to compare to;
		 */
		@Override
		public int compareTo(EdgeWeight o) {
			if (o == null) {
				return 1;
			}
			return o.weight - this.weight;
		}
		
		
		/**
		 * @param obj The object for comparison.
		 * @return The comparison result.
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof EdgeWeight)) {
				return false;
			}
			return ((EdgeWeight) obj).getWeight() == this.weight;
		}

		/**
		 * @return Hash code - just weight number.
		 */
		@Override
		public int hashCode() {
			return this.weight;
		}

		/**
		 * @return Weight value as String.
		 */
		@Override
		public String toString() {
			return String.valueOf(this.weight);
		}
	}
	
	/**
	 * Priority queue implementation based on the heap with functional changes to be used by 
	 * min span tree construction algorithm. Contains internal mapping between destination edge vertices 
	 * and weights. Ensures that only single instance to each destination vertex is allowed (with the lowest 
	 * weight).
	 * @author usun
	 *
	 * @param <K> Key representing a vertex, it will be used for comparisons with
	 *            other vertices for equality. Should be unique in a graph. Cannot
	 *            be null.
	 */
	protected static class WeightedEdgePriorityQueue<K> extends HeapArray<EdgeWeight, Edge<K, EdgeWeight>> {
		
		/**
		 * Contains internal mapping between destination edge vertices 
		 * and weights. Ensures that only single instance to each destination vertex is allowed (with the lowest 
		 * weight).
		 */
		protected Map<K, EdgeWeight> weightsByDestinationVertexKeys;
		
		/**
		 * Default constructor.
		 * @param capacity The max capacity expected, to avoid rehashing.
		 */
		public WeightedEdgePriorityQueue(int capacity) {
			super(capacity);
			this.weightsByDestinationVertexKeys = new HashMap<K, EdgeWeight>(capacity * 2, 0.5f);
		}
		
		/**
		 * Push key and value into the heap (weakly ordered binary tree).
		 * @param weight The key used for comparison and weakly order the underlying tree.
		 * @param edge The data associated with a key.
		 * @return Indication if insertion was successful. 
		 * If we already have an Edge with same or lower weight in the heap for the same destination, 
		 * do not add it and return false. 
		 */
		@Override
		public boolean push(EdgeWeight weight, Edge<K, EdgeWeight> edge) {
			if (weight == null) {
				throw new IllegalArgumentException("Edge weight should be specified.");
			}
			if (edge == null) {
				throw new IllegalArgumentException("Edge should be specified.");
			}
			K destinationKey = edge.getDestinationKey();
			if (this.weightsByDestinationVertexKeys.containsKey(destinationKey)) {
				EdgeWeight existingWeight = this.weightsByDestinationVertexKeys.get(destinationKey);
				if (weight.compareTo(existingWeight) > 0) {
					// Remove existing one.
					removeHeapElement(destinationKey);
					// Replace
					this.weightsByDestinationVertexKeys.put(destinationKey, weight);
					return super.push(weight, edge);
				} else {
					// Keep existing one with lowest weight.
					return false;
				}
			} else {
				this.weightsByDestinationVertexKeys.put(destinationKey, weight);
				return super.push(weight, edge);
			}
		}

		/**
		 * Gets the highest/lowest priority node from the queue and removes it from the queue.
		 * In addition removes weight information related to destination for the associated edge from 
		 * the internal mapping.
		 * @return The node with with highest/lowest priority.
		 */
		@Override
		public HeapNode<EdgeWeight, Edge<K, EdgeWeight>> poll() {
			HeapNode<EdgeWeight, Edge<K, EdgeWeight>> node = super.poll();
			if (node != null && node.getValue() != null && node.getValue().getDestinationKey() != null) {
				this.weightsByDestinationVertexKeys.remove(node.getValue().getDestinationKey());
			}
			return node;
		}
		
		
		
		/**
		 * Remove element from the heap. O(N) speed.
		 * @param key The vertex destination key of the element to remove.
		 */
		protected void removeHeapElement(K key) {
			if (isEmpty()) {
				 return;
			}
			for (int nodeIndex = 0; nodeIndex < this.elementsNumber; nodeIndex++) {
				K elementKey = this.heapArray[nodeIndex].getValue() == null ? null :
					this.heapArray[nodeIndex].getValue().getDestinationKey();
				if (key == null ? elementKey == null : key.equals(elementKey)) {
					this.heapArray[nodeIndex] = this.heapArray[--this.elementsNumber];
					// Checking if any elements are behind removed element.
					if (nodeIndex < this.elementsNumber) {
						bubbleDown(nodeIndex);
					}
				}
			}
		}
		
	}
	
	/**
	 * Default constructor with default expected maximum amount of vertices.
	 * If exceeded, the graph will auto-expand.
	 */
	public WeightedGraphSimple() {
		this(DEFAULT_MAX_SIZE);
	}
	
	/**
	 * Constructor with provided maximum expected amount of vertices. 
	 * If this amount is exceeded, internal graph storage will auto-expand.
	 * @param maxSize The maximum expected amount of vertices. Not a hard limit, 
	 * but it's advised to set it correctly for performance reasons. 
	 */
	public WeightedGraphSimple(int maxSize) {
		super(maxSize);
	}
	
	/**
	 * Link vertices specified under edge object. Edge weight is included.
	 * @param edge The edge object, contains origin and destination vertex keys, direction information, 
	 * and weight. If weight is null or 0, it's considered as not linked/not adjacent vertices.
	 * @return This updated graph object.
	 */
	public WeightedGraph<K, V, EdgeWeight> addEdge(Edge<K, EdgeWeight> edge) {
		if (edge == null) {
			throw new IllegalArgumentException("Edge object should be specified.");
		}
		if (edge.getOriginKey() == null) {
			throw new IllegalArgumentException("Edge object should contain not null origin vertex key.");
		}
		if (edge.getDestinationKey() == null) {
			throw new IllegalArgumentException("Edge object should contain not null destination vertex key.");
		}
		updateAdjacency(edge.getOriginKey(), edge.getDestinationKey(), 
			edge.getWeight() == null ? NOT_ADJACENT : edge.getWeight().getWeight(), 
			edge.isBiDirectional());
		return this;
	}
	
	/**
	 * Construct minimum spanning tree (MST) for this graph, edge weight is taken into account. 
	 * Minimum total weight is targeted.
	 * The graph must be connected, IllegalStateException is thrown otherwise.
	 * @return A new sub-graph containing minimum spanning tree of the original weighted graph. 
	 * If original graph is empty, the empty MST is returned.
	 */
	@Override
	public Graph<K, V> getMinSpanningTree() {
		WeightedGraphSimple<K, V> mst = new WeightedGraphSimple<K, V>(newAdjacencyMatrixMaximumDimension(this.initialMaxSize));
		if (isEmpty()) {
			return mst;
		}
		// Store all vertex indexes added to the tree, we need to mark them as unvisited in the end of the method.
		Set<Integer> vertexIndexesInMST = new HashSet<Integer>();
		try {
			/*
			 * Loop:
			 * 1. Put current vertex into MST (Min Spanning Tree).
			 * 2. Find edges from current vertex to non-connected vertices and add them to the priority queue 
			 * (ordered by edge weights).
			 * 3. Select the edge to vertex not yet in the MST with minimum weight, make it current 
			 * and run steps for it starting from #1.
			 * 
			 * Exit loop conditions:
			 * - priority queue doesn't have anything left, meaning the graph is not connected, IllegalStateException is thrown.
			 * - MST size is the same as graph size, MST was built successfully.
			 */
			
			int currentVertixIndex = 0;
			int graphSize = size();
			// Priority queue ordered by Edge weights.
			WeightedEdgePriorityQueue<K> priorityQueue = new WeightedEdgePriorityQueue<K>(graphSize);
			// Holds edge to add to the tree
			Edge<K, EdgeWeight> currentEdge = null;
			while (mst.size() < graphSize - 1) {
				// Not null, ensured by parent implementation details.
				Vertex<K, V> currentVertex = this.vertices.get(currentVertixIndex);
				// 1. Put current vertex to MST
				vertexIndexesInMST.add(currentVertixIndex);
				mst.addVertex(currentVertex.getKey(), currentVertex.getValue());
				// Using it instead of HashSet for vertexIndexesInMST for little performance gain.
				visitVertex(currentVertixIndex);
				// Add edge
				if (currentEdge != null) {
					mst.addEdge(currentEdge);
					currentEdge = null;
				}
				
				// 2. Find all edges from current vertex to vertices not in MST yet and try
				// adding them to the priority queue (heap) sorted by weights.
				List<Integer> adjacencyRow = this.adjacencyMatrix.get(currentVertixIndex);
				for (int columnIndex = 0; columnIndex < adjacencyRow.size(); columnIndex++) {
					if (columnIndex == currentVertixIndex) {
						// Reflective edge X->X is ignored.
						continue;
					}
					// Weight represented by integer in this simple implementation.
					Integer adjacency = adjacencyRow.get(columnIndex);
					if (adjacency == null || NOT_ADJACENT == adjacency.intValue()) {
						// Not connected, skip.
						continue;
					}
					Vertex<K, V> candidateVertex = this.vertices.get(columnIndex);
					if (candidateVertex.isVisited()) {
						// Already in MST.
						continue;
					}
					EdgeWeight adjacencyWeight = new EdgeWeight(adjacency);
					// Found an edge between current vertex (inside MST) and some connected vertex outside MST.
					Edge<K, EdgeWeight> candidateEdge = new Edge<K, EdgeWeight>(
						currentVertex.getKey(), candidateVertex.getKey(), false, adjacencyWeight);
					priorityQueue.push(adjacencyWeight, candidateEdge);
				} // end of for loop
				
				// 3. Pop edge with min weight
				if (priorityQueue.isEmpty()) {
					throw new IllegalStateException("The graph is unconnected.");
				}
				Edge<K, EdgeWeight> nextVertexEdge = priorityQueue.pollValue();
				currentEdge = nextVertexEdge;
				// Mark next vertex based on the destination of the heaviest/shortest/lightest/... edge. 
				currentVertixIndex = this.vertexLocation.get(nextVertexEdge.getDestinationKey());
				
			} // end of while (mst.size() < graphSize)
			if (currentEdge != null) {
				// Not null, ensured by parent implementation details.
				Vertex<K, V> currentVertex = this.vertices.get(currentVertixIndex);
				// 1. Put current vertex to MST
				vertexIndexesInMST.add(currentVertixIndex);
				mst.addVertex(currentVertex.getKey(), currentVertex.getValue());
				// Add last found edge
				mst.addEdge(currentEdge);
			}
			return mst;
		} finally {
			// Unvisit vertices added to the MST to restore data to the previous state on the original graph.
			vertexIndexesInMST.stream().forEach(index -> unvisitVertex(index));
		}
	}

	public static void main(String[] args) {
		WeightedGraphSimple<String, String> graph = new WeightedGraphSimple<String, String>(10);
		graph.addVertex("A", null).addVertex("B", null).addVertex("C", null).addVertex("D", null)
			.addVertex("E", null).addVertex("F", null);
		graph.addEdge(new Edge<String, EdgeWeight>("A", "B", true, new EdgeWeight(6)))
			.addEdge(new Edge<String, EdgeWeight>("A", "D", true, new EdgeWeight(4)))
			.addEdge(new Edge<String, EdgeWeight>("B", "D", true, new EdgeWeight(7)))
			.addEdge(new Edge<String, EdgeWeight>("B", "E", true, new EdgeWeight(7)))
			.addEdge(new Edge<String, EdgeWeight>("B", "C", true, new EdgeWeight(10)))
			.addEdge(new Edge<String, EdgeWeight>("C", "D", true, new EdgeWeight(8)))
			.addEdge(new Edge<String, EdgeWeight>("C", "E", true, new EdgeWeight(5)))
			.addEdge(new Edge<String, EdgeWeight>("C", "F", true, new EdgeWeight(6)))
			.addEdge(new Edge<String, EdgeWeight>("D", "E", true, new EdgeWeight(12)))
			.addEdge(new Edge<String, EdgeWeight>("E", "F", true, new EdgeWeight(7)));
		graph.traceGraph();
		System.out.println("-------------------");
		
		System.out.println("MST for weighted graph:");
		Graph<String, String> mst = graph.getMinSpanningTree();
		((GraphSimple<String, String>) mst).traceGraph();
		System.out.println("-------------------");
		
	}
}