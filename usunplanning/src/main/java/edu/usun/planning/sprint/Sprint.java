package edu.usun.planning.sprint;

import java.util.Calendar;
import java.util.List;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.release.Release;

/**
 * Represents a sprint and its planning details.
 * 
 * @author usun
 */
public class Sprint extends PlanEntity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Sprint start date. */
	protected Calendar startDate;
	
	/** Sprint end date. */
	protected Calendar endDate;
	
	/** Releases due to integration team / branch during this sprint. */
	protected List<Release> releasesToIntegration;
	
	/** Available teams and their velocity during this sprint. */
	protected List<SprintTeamAvailability> availableVelocities;
	
	/** Actual planning numbers for a team velocity/capacity allocation/assignment for activities during this sprint. */
	protected List<SprintTeamActivityPlan> assignedVelocities;
	
	/** Capacity plan per person per sprint. */
	protected List<SprintPersonCapacity> sprintPersonCapacities;
	
	
	/**
	 * Default constructor.
	 */
	public Sprint() {
		super();
	}


	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the releasesToIntegration
	 */
	public List<Release> getReleasesToIntegration() {
		return releasesToIntegration;
	}


	/**
	 * @param releasesToIntegration the releasesToIntegration to set
	 */
	public void setReleasesToIntegration(List<Release> releasesToIntegration) {
		this.releasesToIntegration = releasesToIntegration;
	}


	/**
	 * @return the availableVelocities
	 */
	public List<SprintTeamAvailability> getAvailableVelocities() {
		return availableVelocities;
	}


	/**
	 * @param availableVelocities the availableVelocities to set
	 */
	public void setAvailableVelocities(List<SprintTeamAvailability> availableVelocities) {
		this.availableVelocities = availableVelocities;
	}


	/**
	 * @return the assignedVelocities
	 */
	public List<SprintTeamActivityPlan> getAssignedVelocities() {
		return assignedVelocities;
	}


	/**
	 * @param assignedVelocities the assignedVelocities to set
	 */
	public void setAssignedVelocities(List<SprintTeamActivityPlan> assignedVelocities) {
		this.assignedVelocities = assignedVelocities;
	}


	/**
	 * @return the sprintPersonCapacities
	 */
	public List<SprintPersonCapacity> getSprintPersonCapacities() {
		return sprintPersonCapacities;
	}


	/**
	 * @param sprintPersonCapacities the sprintPersonCapacities to set
	 */
	public void setSprintPersonCapacities(List<SprintPersonCapacity> sprintPersonCapacities) {
		this.sprintPersonCapacities = sprintPersonCapacities;
	}
	
	
}
