package edu.usun.planning.activity;

import edu.usun.planning.PlanEntityUtils;

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
		return PlanEntityUtils.toStringStandard(this, new String[] {
			"name", "trackingReference", "stream"});
	}
}