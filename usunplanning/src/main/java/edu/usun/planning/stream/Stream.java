package edu.usun.planning.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.activity.Activity;

/**
 * Represents a planning stream, there can be multiple withing customer and platform projects. 
 * E.g. SAS Release 1, SAS Block 0, Platform Maintenance, etc. 
 * Typically a customer go-live with multiple new features constituate a go-live.
 * 
 * @author usun
 */
public class Stream extends PlanEntity {
	
	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Indication for internal mapping if there is no assigned stream registered for an activity. */
	public static final String STREAM_NOT_ASSIGNED = "N/A";
	
	/** 
	 * Known activities to track in planning for the given sprint. Can include features, general support activities like extra UAT, etc.
	 */
	protected List<Activity> activities;
	
	/**
	 * Default constructor.
	 */
	public Stream() {
		super();
	}

	/**
	 * @return the activities
	 */
	public List<Activity> getActivities() {
		return activities;
	}

	/**
	 * @param activities the activities to set
	 */
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	/**
	 * Adds activity for the stream.
	 * @param activity The activity to add.
	 */
	public void addActivity(Activity activity) {
		if (this.activities == null) {
			this.activities = Collections.synchronizedList(new ArrayList<Activity>());
		}
		this.activities.add(activity);
	}
	
}
