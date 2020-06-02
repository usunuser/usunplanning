package edu.usun.planning.activity;

/**
 * Task (usually represents on demand support activity, e.g. UAT bug fixing bucket), 
 * as opposed to a feature which has a specific estimate.
 * 
 * @author usun
 */
public class Task extends Activity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Default constructor.
	 */
	public Task() {
		super();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer()
			.append("Task{")
			.append("name=").append(this.getName()).append(',')
			.append("trackingReference=").append(this.getTrackingReference()).append(',')
			.append("stream=").append(this.getStream())
			.append('}').toString();
	}
}