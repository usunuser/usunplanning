package edu.usun.planning.activity;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.PlanEntityUtils;
import edu.usun.planning.stream.Stream;

/**
 * Basic activity entity (can be extended to represent features, tasks and other entities).
 * 
 * @author usun
 */
public abstract class Activity extends PlanEntity {
	
	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Reference to a ticket within a tracking system corresponding to this planned activity 
	 * (e.g. JIRA epic number). Usually refers to items within main internal/company tracking system.
	 */
	protected String trackingReference;
	
	/**
	 * Stream / project under which activity is tracked. 
	 * It effectively means we cannot reuse the same generic entity assigned to multiple streams at once 
	 * (e.g. extra planned support should be separated by streams).
	 */
	protected Stream stream;
	
	/**
	 * Default constructor.
	 */
	public Activity() {
		super();
	}

	/**
	 * @return the trackingReference
	 */
	public String getTrackingReference() {
		return trackingReference;
	}

	/**
	 * @param trackingReference the trackingReference to set
	 */
	public void setTrackingReference(String trackingReference) {
		this.trackingReference = trackingReference;
	}

	/**
	 * @return the stream
	 */
	public Stream getStream() {
		return stream;
	}

	/**
	 * @param stream the stream to set
	 */
	public void setStream(Stream stream) {
		this.stream = stream;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PlanEntityUtils.toStringStandard(this, new String[] {
			"name", "trackingReference", "stream"});
	}
}
