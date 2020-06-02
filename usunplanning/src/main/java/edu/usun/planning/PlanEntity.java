package edu.usun.planning;

import java.io.Serializable;

/**
 * Base class for main planning entity objects.
 * 
 * @author usun
 */
public abstract class PlanEntity implements Serializable, Cloneable {

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the entity.
	 */
	protected String name;

	/**
	 * Default constructor.
	 */
	public PlanEntity() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cloned object.
	 */
	public Object clone() throws CloneNotSupportedException {
		PlanEntity copy = (PlanEntity) super.clone();
		return copy;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer()
			.append("PlanEntity{")
			.append("name=").append(this.getName())
			.append('}').toString();
	}
}
