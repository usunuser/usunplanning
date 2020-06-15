package edu.usun.common.graph;

public class Edge<K, W extends Comparable<? super W>> {

	protected K originKey;
	protected K destinationKey;
	protected boolean biDirectional;
	protected W weight;

	public Edge(K originKey, K destinationKey, boolean biDirectional, W weight) {
		super();
		this.originKey = originKey;
		this.destinationKey = destinationKey;
		this.biDirectional = biDirectional;
		this.weight = weight;
	}
	
	public K getOriginKey() {
		return originKey;
	}

	public void setOriginKey(K originKey) {
		this.originKey = originKey;
	}

	public K getDestinationKey() {
		return destinationKey;
	}

	public void setDestinationKey(K destinationKey) {
		this.destinationKey = destinationKey;
	}

	public boolean isBiDirectional() {
		return biDirectional;
	}

	public void setBiDirectional(boolean biDirectional) {
		this.biDirectional = biDirectional;
	}
	
	public W getWeight() {
		return weight;
	}

	public void setWeight(W weight) {
		this.weight = weight;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.originKey);
		if (this.biDirectional) {
			sb.append('<');
		}
		sb.append("->").append(this.destinationKey).append('=').append(this.weight);
		return sb.toString();
	}
}
