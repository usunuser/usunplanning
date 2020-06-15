package edu.usun.common.util;

import java.lang.reflect.Array;

/**
 * In-memory heap data structure with extendible array as its basis.
 * Null keys or values are not allowed.
 * This implementation is not thread safe.
 * 
 * @author usun
 *
 * @param <K> Key type, should be implementing Comparable interface, since heap structure is weakly ordered 
 * and these keys are used to impose that weak structure.
 * @param <V> A value object holding data.
 */
public class HeapArray<K extends Comparable<? super K>, V> {

	/**
	 * Heap data structure node.
	 * @author usun
	 *
	 * @param <K> Represents a key to be used to build a heap data structure, it should be a Comparable key.
	 * @param <V> The business value object. Can be null.
	 */
	public static class HeapNode<K extends Comparable<? super K>, V> {
		/**
		 * Heap node key.
		 */
		protected K key;
		/**
		 * Business data object.
		 */
		protected V value;
		
		/**
		 * @param key The node key.
		 * @param value The business data value object.
		 */
		public HeapNode(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		/**
		 * @return The node key.
		 */
		public K getKey() {
			return key;
		}

		/**
		 * @param key The node key.
		 */
		public void setKey(K key) {
			this.key = key;
		}

		/**
		 * @return The business data value object.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * @param value The business data value object.
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * @return String representation.
		 */
		@Override
		public String toString() {
			return this.key + "=" + this.value;
		}
	}
	
	/** Default size (amount of nodes) of heap allocated.  */
	protected static final int DEFAULT_CAPACITY = 16;
	/** Max possible capacity for the heap.  */
	protected static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
	
	/**
	 * Tracks meaningful elements in internal storage, which may 
	 * contain ghost objects we'd like to ignore.
	 */
	protected int elementsNumber;
	
	/**
	 * Expandable array to hold heap Node objects (Key and Value).
	 * Field elementsNumber should be used to identify the last usable object in the array, 
	 * as old elements are not removed from the tail of the heap list to speed up performance, even 
	 * though they will hold extra memory.
	 */
	protected HeapNode<K, V>[] heapArray;
	
	/**
	 * Holding reference to the Class object of the parameterized Node class, 
	 * is used for empty arrays creation for nodes storage.
	 */
	@SuppressWarnings("unchecked")
	protected final Class<HeapNode<K, V>> nodeClazz = 
		(Class<HeapNode<K, V>>) new HeapNode<K, V>(null, null).getClass();
	
	/**
	 * Heap with default allocated initial capacity.
	 */
	public HeapArray() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Heap with specified initial allocated capacity for internal array.
	 * @param capacity The capacity to allocate for internal array.
	 */
	public HeapArray(int capacity) {
		super();
		this.elementsNumber = 0;
		this.heapArray = createEmptyArray(this.nodeClazz, capacity);
		
	}
	
	/**
	 * Ensure enough array capacity is allocated, if not - allocate the double of targetCapacity.
	 * @param targetCapacity The target capacity.
	 */
	protected void ensureEnoughCapacity(int targetCapacity) {
		if (targetCapacity <= 0 || targetCapacity * 2 > MAX_CAPACITY) {
			throw new IllegalArgumentException("Target capacity [" + targetCapacity + 
				"] should be > 0 and <= " + MAX_CAPACITY / 2);
		}
		if (targetCapacity > this.heapArray.length) {
			// Expand a double of what is next needed
			HeapNode<K, V>[] heapArrayNew = createEmptyArray(this.nodeClazz, targetCapacity * 2);
			System.arraycopy(this.heapArray, 0, heapArrayNew, 0, this.elementsNumber);
			this.heapArray = heapArrayNew;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	protected HeapNode<K, V>[] createEmptyArray(Class<HeapNode<K, V>> clazz, int size) {
		return (HeapNode<K, V>[]) Array.newInstance(clazz, size);
	}
	
	/**
	 * @return Checks if empty.
	 */
	public boolean isEmpty() {
		return this.elementsNumber <= 0;
	}
	
	/**
	 * @return Number of elements in the priority queue.
	 */
	public int size() {
		return this.elementsNumber;
	}
	
	/**
	 * Push key and value into the heap (weakly ordered binary tree).
	 * @param key The key used for comparison and weakly order the underlying tree.
	 * @param value The data associated with a key.
	 * @return Indication if insertion was successful. Should always be true in this implementation 
	 * as otherwise a runtime exception is thrown.
	 */
	public boolean push(K key, V value) {
		if (key == null || value == null) {
			throw new IllegalArgumentException("Null keys and values are not allowed.");
		}
		ensureEnoughCapacity(this.elementsNumber + 1);
		
		/*
		 * Put new element at the end (bottom) of the area filled with elements representing a binary weakly ordered tree.
		 * Then bubble it up the tree levels until heap weakly order conditions are met (every parent 
		 * node should have key value bigger than values of keys of its children nodes).
		 */
		HeapNode<K, V> newNode = new HeapNode<K,V>(key, value);
		this.heapArray[this.elementsNumber] = newNode;
		bubbleUp(this.elementsNumber++);
		return true;
	}
	
	/**
	 * Gets the highest/lowest priority node from the queue and removes it from the queue.
	 * @return The node key.
	 */
	public K pollKey() {
		HeapNode<K, V> node = poll();
		return node == null ? null : node.getKey();
	}
	
	/**
	 * Gets the highest/lowest priority node from the queue and removes it from the queue.
	 * @return The node business value object.
	 */
	public V pollValue() {
		HeapNode<K, V> node = poll();
		return node == null ? null : node.getValue();
	}
	
	/**
	 * Gets the highest/lowest priority node from the queue and removes it from the queue.
	 * @return The node with with highest/lowest priority.
	 */
	public HeapNode<K, V> poll() {
		if (isEmpty()) {
			 return null;
		}
		
		/*
		 * Remove root (top) element of the area filled with elements representing a binary weakly ordered tree.
		 * Replace it with the last item in the heap array.
		 * Then bubble it down the tree levels until heap weakly order conditions are met (every parent 
		 * node should have key value bigger than values of keys of its children nodes).
		 */
		HeapNode<K, V> root = this.heapArray[0];
		this.heapArray[0] = this.heapArray[--this.elementsNumber];
		if (!isEmpty()) {
			bubbleDown(0);
		}
		return root;
	}
	

	
	protected void bubbleDown(int nodeIndex) {
		if (nodeIndex < 0 || nodeIndex >= this.elementsNumber) {
			throw new ArrayIndexOutOfBoundsException("Node index [" + 
				nodeIndex + "] out of bounds, should be >=0 and <" + this.elementsNumber);
		}
		
		HeapNode<K, V> topNode = this.heapArray[nodeIndex];
		K topKey = topNode.getKey();
		while (nodeIndex < this.elementsNumber / 2) {
			int leftChildIndex = nodeIndex * 2 + 1;
			K leftChildKey = this.heapArray[leftChildIndex].getKey();
			int rightChildIndex = leftChildIndex + 1;
			K rightChildKey = this.heapArray[rightChildIndex].getKey();
			
			K biggerChildKey = leftChildKey;
			int biggerChildIndex = leftChildIndex;
			
			if (rightChildKey.compareTo(leftChildKey) > 0) {
				biggerChildKey = rightChildKey;
				biggerChildIndex = rightChildIndex;
			}
			
			if (topKey.compareTo(biggerChildKey) < 0) {
				this.heapArray[nodeIndex] = this.heapArray[biggerChildIndex];
				nodeIndex = biggerChildIndex;
			} else {
				break;
			}
		}
		
		this.heapArray[nodeIndex] = topNode;
	}
	
	

	protected void bubbleUp(int nodeIndex) {
		if (nodeIndex < 0 || nodeIndex >= this.elementsNumber) {
			throw new ArrayIndexOutOfBoundsException("Node index [" + 
				nodeIndex + "] out of bounds, should be >=0 and <" + this.elementsNumber);
		}
		if (nodeIndex == 0) {
			// Root already
			return;
		}
		
		/*
		 * Index structure representing a tree:
		 * 	                0
		 * 			1				2
		 * 		3		4		5		6
		 * 	  7  8    9  10   11 12   13 14
		 * 
		 * left child index = 2*nodeIndex + 1, right child index = 2*nodeIndex + 2
		 * parent index = (x - 1) / 2
		 * We ensure that node in the given index has key in a weakly ordered way upwards (lower than a key of a parent).
		 * If not so - swap until find a correct place within a tree.
		 */
		HeapNode<K, V> bottomNode = this.heapArray[nodeIndex];
		K bottomKey = bottomNode.getKey();
		while (nodeIndex > 0) {
			int parentIndex = (nodeIndex - 1) / 2;
			K parentKey = this.heapArray[parentIndex].getKey();
			if (bottomKey.compareTo(parentKey) > 0) {
				// Heap tree rule violated, bringing parent down opposite direction.
				this.heapArray[nodeIndex] = this.heapArray[parentIndex];
				nodeIndex = parentIndex;
			} else {
				// Found a place to insert the original assessed node.
				break;
			}
		}
		// We stored original insertion element in bottomNode to save memory space for interim swap operations. 
		this.heapArray[nodeIndex] = bottomNode;
	}
	
	protected void traceHeap() {
		for (int i = 0; i < this.elementsNumber; i++) {
			if (i > 0) {
				System.out.print(',');
			}
			HeapNode<K, V> node = this.heapArray[i];
			if (node == null) {
				System.out.print("null");
			} else {
				System.out.print(node.getKey() + "=" + node.getValue());
			}
		}
		System.out.println();
	}
	
	protected void traceHeapStructured(boolean includeKeys, boolean includeValues) {
		/*
		 * Index structure representing a tree:
		 * 	                0
		 * 			1				2
		 * 		3		4		5		6
		 * 	  7  8    9  10   11 12   13 14
		 * 
		 * last element on each row: 1, 2, 4, 8, ...
		 */
		int currentIndex = 0;
		// raising by power of 2
		int elementsPerRow = 1;
		// current count (not index) for element in the current row
		int elementsInCurrentRow = 0;
		
		while (currentIndex < this.elementsNumber) {
			if (elementsInCurrentRow > 0) {
				System.out.print(' ');
			}
			HeapNode<K, V> node = this.heapArray[currentIndex];
			if (node == null) {
				System.out.print("null");
			} else {
				StringBuffer sb = new StringBuffer();
				if (includeKeys) {
					sb.append(node.getKey());
				}
				if (includeKeys && includeValues) {
					sb.append('=');
				}
				if (includeValues) {
					sb.append(node.getValue());
				}
				if (includeKeys || includeValues) {
					System.out.print(sb.toString());
				}
			}
			
			currentIndex++;
			elementsInCurrentRow++;
			if (elementsInCurrentRow >= elementsPerRow) {
				// This row ended.
				elementsInCurrentRow = 0;
				System.out.println();
			}
			if (elementsInCurrentRow == 0) {
				// Multiply elements limit by 2 for the next row.
				elementsPerRow = elementsPerRow << 1;
				if (elementsPerRow < 0) {
					System.out.println("Elements overflow.");
					break;
				}
			}
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		HeapArray<Integer, String> priorityQueue = new HeapArray<Integer, String>(2);
		priorityQueue.push(5, "medium priority item");
		priorityQueue.push(1, "very low priority item");
		priorityQueue.push(10, "high priority item");
		priorityQueue.push(3, "low priority item");
		priorityQueue.push(25, "very high priority item");
		priorityQueue.push(7, "medium priority item");
		priorityQueue.push(55, "very high priority item");
		priorityQueue.push(55, "dup very high priority item");
		priorityQueue.push(2, "very low priority item");
		priorityQueue.push(2, "dup very low priority item");
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
		System.out.println("Removing: " + priorityQueue.pollKey());
		priorityQueue.traceHeapStructured(true, false);
		
	}
}
