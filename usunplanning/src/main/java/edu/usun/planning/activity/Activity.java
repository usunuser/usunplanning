/**
 * 
 */
package edu.usun.planning.activity;

import edu.usun.planning.PlanEntity;
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
	 * Reference to JIRA.
	 */
	protected String jiraReference;
	
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
	 * @return the jiraReference
	 */
	public String getJiraReference() {
		return jiraReference;
	}

	/**
	 * @param jiraReference the jiraReference to set
	 */
	public void setJiraReference(String jiraReference) {
		this.jiraReference = jiraReference;
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
	
	
}
